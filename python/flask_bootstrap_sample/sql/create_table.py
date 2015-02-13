# -*- coding=utf-8 -*-
__author__ = 'skobayashi1'

import sys
sys.path.append('/Users/skobayashi1/workspace/work/github/SampleApps/python/flask_bootstrap_sample')

from main import *


db.create_all()
admin = User('admin', 'admin@example.com')
guest = User('guest', 'guest@example.com')

db.session.add(admin)
db.session.add(guest)
db.session.commit()
