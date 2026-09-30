from scanner import scan_url

results = scan_url("https://example.com")

print(results["results"]["violations"])