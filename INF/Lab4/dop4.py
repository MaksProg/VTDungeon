import sys
import time

import myjson as json
from myyaml import Yaml as yaml
from dop2 import myjson as json2
from dop2.myyaml import Yaml as yaml2
from dop1 import parse

def test(f, string):
    start_time = time.time()
    for _ in range(100):
        f(string)
    return time.time() - start_time


main_task_parser = lambda x: yaml.dump(json.loads(x))
regex_task_parser = lambda x: yaml2.dump(json2.loads(x))

if __name__ == "__main__":
    with open("in.json", "r") as f:
        data = f.read()

    print(
        f"Own Parser: {test(main_task_parser, data)} seconds",
        f"Own Parser with Regex: {test(regex_task_parser, data)} seconds",
        f"Python JSON Lib + yaml: {test(parse, data)} seconds",
        sep="\n",
    )
