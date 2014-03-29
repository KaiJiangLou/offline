#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os
import json
import jieba
import jieba.analyse

def read_json_file(file_name):
    f = open(file_name)
    content = f.read()
    f.close()
    return json.loads(content)

def write_dict(file_name, the_dict):
    the_tuple = the_dict.items()
    the_tuple.sort(key = lambda x: x[1])
    f = open(file_name, 'w')
    for (key, value) in the_tuple:
        f.write('%d\t%s\n' % (value, key))
    f.close()

def segment_content(content, separator='|'):
    seg_list = jieba.cut(content, cut_all=False)
    clean_list = []
    for word in seg_list:
        word = word.strip()
        if not word or word.isdigit():
            continue
        clean_list.append(word)
    return separator.join(clean_list)

def readdir_and_write2file(dir_name, output_file_name, separator='\t'):
    if not os.path.isdir(dir_name):
        print '%s is not a directory!' % dir_name
        sys.exit(1)
    output_f = open(output_file_name, 'w')
    id2url_dict = {}
    for file_name in os.listdir(dir_name):
        if file_name.startswith('.'):
            continue
        file_name = dir_name + '/' + file_name
        print 'Segmenting file %s ...' % file_name
        post = read_json_file(file_name)
        post_id = id2url_dict.setdefault(post['url'], len(id2url_dict)+1)
        output_f.write(str(post_id)+separator+segment_content(post['content'])+'\n')
    output_f.close()
    write_dict(output_file_name + '.id2url', id2url_dict)

def print_help(argv):
    print 'python %s: <input_file> <output_file>' % argv[0]

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print_help(sys.argv)
        sys.exit(-1)
    input_dir = sys.argv[1]
    output_file_name = sys.argv[2]
    reload(sys)
    sys.setdefaultencoding('utf-8')

    readdir_and_write2file(input_dir, output_file_name)
    #print post['title']
    #print post['content']
    #tags = jieba.analyse.extract_tags(post['title'] + post['content'], 20)
    #print 'tags %s' % ','.join(tags)

