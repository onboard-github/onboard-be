#!/usr/bin/env python3
import sys
import yaml
import json

def fix_examples(res: dict):
    for key, value in res.items():
        if isinstance(value, dict):
            fix_examples(value)
        if key == "application/json":
            for example_name, content in value["examples"].items():
                try:
                    content["value"] = json.loads(content["value"])
                except:
                    pass

with open(sys.argv[1], "r", encoding='utf-8') as api_file:
    res = yaml.safe_load(api_file)
    fix_examples(res)
    print(yaml.dump(res))
