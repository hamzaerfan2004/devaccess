from fastapi.testclient import TestClient
from main import app
from unittest.mock import patch

client = TestClient(app)

def test_health_check():
    response = client.get("/health")

    assert response.status_code == 200
    assert response.json() == {
        "status": "analysis service is running"
    }

@patch("main.scan_url")
def test_create_scan(mock_scan_url):
    mock_scan_url.return_value = {
        "status": "completed",
        "results": {
            "violations": [],
            "passes": [],
            "incomplete": [],
            "inapplicable": []
        },
        "error": None
    }

    response = client.post(
        "/scan",
        json={"url": "https://example.com"}
    )

    assert response.status_code == 200

    assert response.json() == {
        "status": "completed",
        "results": {
            "violations": [],
            "passes": [],
            "incomplete": [],
            "inapplicable": []
        },
        "error": None
    }

@patch("main.scan_url")
def test_create_scan_failure(mock_scan_url):
    mock_scan_url.return_value = {
        "status": "failed",
        "results": None,
        "error": "Page.goto: net::ERR_CONNECTION_TIMED_OUT"
    }

    response = client.post(
        "/scan",
        json={"url": "https://10.255.255.1"}
    )

    assert response.status_code == 200
    assert response.json() == {
        "status": "failed",
        "results": None,
        "error": "Page.goto: net::ERR_CONNECTION_TIMED_OUT"
    }

