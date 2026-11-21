from scanner import scan_url

results = scan_url("https://example.com")

if results["status"] != "completed":
    print("Scan failed:", results["error"])
else:
    print(results["results"]["violations"])
