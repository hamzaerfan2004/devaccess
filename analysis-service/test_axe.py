from pathlib import Path
from scanner import scan_url
from transformer import transform_axe

results = scan_url(Path("test_page.html").resolve().as_uri())

transformed_results = transform_axe(results["results"])

print(transformed_results)