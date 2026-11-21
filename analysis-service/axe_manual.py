from scanner import scan_url
from transformer import transform_axe

results = scan_url("https://example.com")

if results["status"] != "completed":
    print("Scan failed:", results["error"])
else:
    transformed_results = transform_axe(results["results"])
    print(transformed_results)
