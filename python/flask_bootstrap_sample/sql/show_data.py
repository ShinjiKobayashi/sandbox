# -*- coding=utf-8 -*-
__author__ = 'skobayashi1'

import sys
sys.path.append('/Users/skobayashi1/workspace/work/github/SampleApps/python/flask_bootstrap_sample')

from main import *


users = User.query.all()
for user in users:
    print user