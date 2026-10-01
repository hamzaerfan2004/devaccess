from fastapi import FastAPI
from scan_request import ScanRequest
from scanner import scan_url
from transformer import transform_axe

app = FastAPI()

@app.get("/health")
def health_check():
    return {"status": "analysis service is running"}

@app.post("/scan")
def create_scan(request: ScanRequest):
    scan_result = scan_url(request.url)
    if scan_result["status"] == "completed" :
        transform_result = transform_axe(scan_result["results"])
        return {
            "status": "completed",
            "results": transform_result,
            "error": None}
    else:
        return {
            "status": "failed",
            "results": None,
            "error": scan_result["error"]
        }