def transform_node(node):
    return {
        "html": node["html"],
        "target": node["target"],
        "impact": node["impact"],
        "failureSummary": node["failureSummary"]
    }

def transform_violation(violation):
    return {
        "ruleId": violation["id"],
        "impact": violation["impact"],
        "description": violation["description"],
        "help": violation["help"],
        "helpUrl": violation["helpUrl"],
        "nodes": [transform_node(node) for node in violation["nodes"]]
    }

def transform_axe(results):
    violations = [
        transform_violation(violation)
        for violation in results["violations"]
    ]

    incomplete = [
        transform_violation(item)
        for item in results["incomplete"]
    ]

    return {
        "violations": violations,
        "incomplete": incomplete
    }