import sys
sys.path.append("..")

import os
from dop2.myyaml import Yaml as yaml
import dop2.myjson as json

def parse(string):
    return yaml.dump(json.loads(string))

if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "..\in.json")
    output_file = os.path.join(os.path.dirname(__file__), "..\out-dop2.yaml")

    string = open(input_file, "r",encoding='utf-8').read()
    open(output_file, "w",encoding='utf-8').write(parse(string))
    print("dop2.main complete!")