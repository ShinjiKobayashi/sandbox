#!/usr/bin/env python
# -*- coding: utf-8 -*-
__author__ = 'skobayashi'

from flask import Flask, render_template, session, redirect, url_for, escape, request
from flask.ext.sqlalchemy import SQLAlchemy
import os

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:////Users/skobayashi1/workspace/work/github/SampleApps/python/flask_bootstrap_sample/db/test.db'
db = SQLAlchemy(app)
app.secret_key = os.urandom(24)

from app.models.User import User


@app.route('/')
def root_index():
    if "username" in session:
        return "Login as {} ".format(escape(session["username"]))
    return render_template("index.html")


@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        print request.form
        session['username'] = request.form['username']
        return redirect(url_for('root_index'))
    else:
        return redirect(url_for('root_index'))


@app.route('/logout', methods=['GET', 'POST'])
def logout():
    session.pop("username", None)
    return redirect(url_for('root_index'))


@app.route('/register')
def register():
    print request.form
    return "requested : ", request.form

if __name__ == "__main__":
    app.run(debug=True)