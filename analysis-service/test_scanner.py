import pytest
from url_validator import is_public_ip, validate_url
import socket
from types import SimpleNamespace
from unittest.mock import patch
from scanner import handle_route, scan_url

@pytest.mark.parametrize("ip_address", [
    "8.8.8.8",
    "1.1.1.1",
    "2606:4700:4700::1111"
])
def test_public_ip_addresses_are_allowed(ip_address):
    assert is_public_ip(ip_address) is True


@pytest.mark.parametrize("ip_address", [
    "127.0.0.1",
    "192.168.1.1",
    "10.0.0.1",
    "169.254.1.1",
    "172.16.0.1",
    "::1",
    "fc00::1",
    "fe80::1",
    "203.0.113.1"
])
def test_non_public_ip_addresses_are_blocked(ip_address):
    assert is_public_ip(ip_address) is False

@pytest.mark.parametrize("url", [
    "https://example.com",
    "http://example.com",
    "https://example.com/accessibility",
    "https://8.8.8.8"
])
def test_valid_public_urls_are_accepted(url, monkeypatch):
    monkeypatch.setattr(
        "url_validator.socket.getaddrinfo",
        lambda *args, **kwargs: [
            (socket.AF_INET, socket.SOCK_STREAM, 6, "", ("93.184.216.34", 0))
        ]
    )

    assert validate_url(url) is True


@pytest.mark.parametrize("url", [
    "ftp://example.com",
    "file:///etc/passwd",
    "localhost",
    "http://localhost",
    "http://127.0.0.1",
    "http://192.168.1.1",
    "http://10.0.0.1",
    "http://[::1]",
    "https://",
    "not-a-url",
    "https://user:password@example.com"
])
def test_invalid_or_unsafe_urls_are_rejected(url):
    with pytest.raises(ValueError):
        validate_url(url)

def test_domain_resolving_to_private_ip_is_rejected(monkeypatch):
    monkeypatch.setattr(
        "url_validator.socket.getaddrinfo",
        lambda *args, **kwargs: [
            (socket.AF_INET, socket.SOCK_STREAM, 6, "", ("192.168.1.10", 0))
        ]
    )

    with pytest.raises(ValueError):
        validate_url("https://example.com")


def test_domain_with_mixed_public_and_private_ips_is_rejected(monkeypatch):
    monkeypatch.setattr(
        "url_validator.socket.getaddrinfo",
        lambda *args, **kwargs: [
            (socket.AF_INET, socket.SOCK_STREAM, 6, "", ("93.184.216.34", 0)),
            (socket.AF_INET, socket.SOCK_STREAM, 6, "", ("10.0.0.5", 0))
        ]
    )

    with pytest.raises(ValueError):
        validate_url("https://example.com")


def test_domain_with_no_dns_records_is_rejected(monkeypatch):
    def failed_dns_lookup(*args, **kwargs):
        raise socket.gaierror("Name or service not known")

    monkeypatch.setattr(
        "url_validator.socket.getaddrinfo",
        failed_dns_lookup
    )

    with pytest.raises(ValueError):
        validate_url("https://example.com")


class FakeRoute:
    def __init__(self, url):
        self.request = SimpleNamespace(url=url)
        self.aborted = False
        self.continued = False

    def abort(self):
        self.aborted = True

    def continue_(self):
        self.continued = True

@patch("scanner.validate_url")
def test_safe_browser_request_is_allowed(mock_validate_url):
    route = FakeRoute("https://example.com")

    handle_route(route)

    mock_validate_url.assert_called_once_with("https://example.com")
    assert route.continued is True
    assert route.aborted is False

@patch("scanner.validate_url")
def test_unsafe_browser_request_is_blocked(mock_validate_url):
    mock_validate_url.side_effect = ValueError("Unsafe destination")

    route = FakeRoute("http://192.168.1.1")

    handle_route(route)

    mock_validate_url.assert_called_once_with("http://192.168.1.1")
    assert route.aborted is True
    assert route.continued is False

@patch("scanner.sync_playwright")
@patch("scanner.validate_url")
def test_unsafe_initial_url_is_rejected_before_browser_launch(
    mock_validate_url,
    mock_sync_playwright
):
    mock_validate_url.side_effect = ValueError(
        "Private or reserved IP addresses are not allowed"
    )

    result = scan_url("http://127.0.0.1")

    assert result["status"] == "failed"
    assert result["results"] is None
    assert result["error"] == "Private or reserved IP addresses are not allowed"

    mock_sync_playwright.return_value.__enter__.return_value.chromium.launch.assert_not_called()
