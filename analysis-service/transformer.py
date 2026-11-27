def transform_node(node):
    return {
        "html": node.get("html"),
        "target": node.get("target", []),
        "impact": node.get("impact"),
        "failureSummary": node.get("failureSummary")
    }


def transform_rule_result(rule):
    return {
        "ruleId": rule["id"],
        "impact": rule.get("impact"),
        "description": rule["description"],
        "help": rule["help"],
        "helpUrl": rule["helpUrl"],
        "nodes": [transform_node(node) for node in rule["nodes"]]
    }


def transform_axe(results):
    passes = [
        transform_rule_result(item)
        for item in results["passes"]
    ]

    violations = [
        transform_rule_result(item)
        for item in results["violations"]
    ]

    incomplete = [
        transform_rule_result(item)
        for item in results["incomplete"]
    ]

    return {
        "passes": passes,
        "violations": violations,
        "incomplete": incomplete
    }