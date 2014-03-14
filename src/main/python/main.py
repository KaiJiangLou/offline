#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import json
import jieba
import jieba.analyse

def read_json_file(file_name):
    f = open(file_name)
    content = f.read()
    f.close()
    return json.loads(content)

if __name__ == '__main__':
    file_name = sys.argv[1]
    post = read_json_file(file_name)
    print post['title']
    print post['content']
    tags = jieba.analyse.extract_tags(post['title'] + post['content'], 20)
    print 'tags %s' % ','.join(tags)

