
import socket
from ipaddress import ip_address
from urllib.parse import urlsplit


def is_public_ip(value):
    try:
        address = ip_address(value)
    except ValueError:
        return False

    return address.is_global and not address.is_multicast


def validate_url(url):
    try:
        parsed_url = urlsplit(url)

        if parsed_url.scheme not in ("http", "https"):
            raise ValueError("Only HTTP and HTTPS URLs are allowed")

        if not parsed_url.hostname:
            raise ValueError("URL must contain a hostname")

        if parsed_url.username or parsed_url.password:
            raise ValueError("URLs containing credentials are not allowed")

        port = parsed_url.port

        hostname = parsed_url.hostname.lower()

        if hostname == "localhost" or hostname.endswith(".localhost"):
            raise ValueError("Localhost URLs are not allowed")

        try:
            ip_address(hostname)
            is_ip_address = True
        except ValueError:
            is_ip_address = False

        if is_ip_address:
            if not is_public_ip(hostname):
                raise ValueError(
                    "Private or reserved IP addresses are not allowed"
                )
        else:
            if port is None:
                port = 443 if parsed_url.scheme == "https" else 80

            try:
                addresses = socket.getaddrinfo(
                    hostname,
                    port,
                    type=socket.SOCK_STREAM
                )
            except OSError as error:
                raise ValueError("Hostname could not be resolved") from error

            if not addresses:
                raise ValueError("Hostname did not resolve to any IP addresses")

            for address in addresses:
                resolved_ip = address[4][0]

                if not is_public_ip(resolved_ip):
                    raise ValueError(
                        "Hostname resolves to a private or reserved IP address"
                    )

        return True

    except ValueError as error:
        raise ValueError(str(error)) from error
