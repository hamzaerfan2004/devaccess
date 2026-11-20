from playwright.sync_api import Error as PlaywrightError
from playwright.sync_api import sync_playwright

def scan_url(url):
    with sync_playwright() as p:
        browser = p.chromium.launch()
        try:
            page = browser.new_page()

            page.goto(url, timeout=30000)

            page.add_script_tag(path="node_modules/axe-core/axe.min.js")

            results = page.evaluate("axe.run()")

            return {
                "status": "completed",
                "results": results,
                "error": None
            }

        except PlaywrightError as error:
            return {"status": "failed", "results": None, "error": str(error)}
        finally:
            if browser is not None:
                browser.close()