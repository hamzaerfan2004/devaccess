from playwright.sync_api import Error as PlaywrightError
from playwright.sync_api import sync_playwright
from url_validator import validate_url

def scan_url(url):
    with sync_playwright() as p:
        browser = None

        try:
            validate_url(url)

            browser = p.chromium.launch()

            context = browser.new_context(service_workers="block")
            context.route("**/*", handle_route)

            page = context.new_page()
            page.goto(url, timeout=30000)

            page.add_script_tag(path="node_modules/axe-core/axe.min.js")

            results = page.evaluate("axe.run()")

            return {
                "status": "completed",
                "results": results,
                "error": None
            }

        except ValueError as error:
            return {"status": "failed", "results": None, "error": str(error)}

        except PlaywrightError as error:
            return {"status": "failed", "results": None, "error": str(error)}

        finally:
            if browser is not None:
                browser.close()

def handle_route(route):
    try:
        validate_url(route.request.url)
    except ValueError:
        route.abort()
    else:
        route.continue_()
