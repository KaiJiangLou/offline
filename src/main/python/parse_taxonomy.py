#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import json
import codecs

from segment_content import *

class TaxonomyParser:
    CAT_KEYWORD_DICT = {
            '移动开发': ['移动开发', 'lbs'],
            '游戏开发': ['游戏开发', '手游开发'],
            '前端开发': ['前端开发', 'html5'],
            '通信技术': ['通信系统', '移动通信'],
            '软件开发': ['软件开发'],
            '机器学习': ['机器学习', '数据挖掘', '机器翻译'],
            '自然语言处理': ['自然语言处理', 'nlp', '机器翻译'],
            '搜索引擎': ['搜索引擎', 'search engine'],
            '大数据': ['开源技术', 'hbase', 'mongodb', 'redis', 'hadoop', 'hive'],
            '项目管理': ['项目管理'],
            '艺术': ['摄影', '书法', '绘画', '工笔画', '画展', '插花', '旅行', '艺术家', '艺术展', '花卉展览', '广告设计', '平面设计', '话剧']
            }

    COMPANIES = ['google', '谷歌', 'facebook', 'linkedin', 'yahoo', '微软', 'twitter', 'netflix', 'hulu', 'youtube', '百度', '阿里巴巴', '腾讯', '奇虎360', '小米', '优酷', '土豆', '阿里云', '华为', '搜狗']

    LOCATIONS = ['北京', '上海', '杭州', '大连', '深圳', '广州', '成都']

    def __init__(self, post_json=None):
        self.post = post_json

    def set_post(self, post_json):
        self.post = post_json

    def parse_categories(self):
        if not self.post:
            return []
        cat_from_title = self._parse_categories(self.post['title'].lower())
        if cat_from_title:
            return cat_from_title
        return self._parse_categories(self.post['content'].lower())

    def _parse_categories(self, content):
        #content = content.lower()
        results = []
        for (category, keywords) in self.CAT_KEYWORD_DICT.iteritems():
            if self._contain_some_keyword(content, keywords):
                results.append(category)
        return results

    def _contain_some_keyword(self, content, keywords):
        contained = []
        for keyword in keywords:
            if keyword in content:
                contained.append(keyword)
        return contained

    def parse_companies(self):
        if not self.post:
            return []
        kws_from_title = self._contain_some_keyword(self.post['title'].lower(), self.COMPANIES)
        if kws_from_title:
            return kws_from_title
        return self._contain_some_keyword(self.post['content'].lower(), self.COMPANIES)

    def parse_locations(self):
        if not self.post:
            return []
        kws_from_address = self._contain_some_keyword(self.post['address'].lower(), self.LOCATIONS)
        if kws_from_address:
            return kws_from_address
        return self._contain_some_keyword(self.post['title'].lower(), self.LOCATIONS)


def write_json_file(dict_or_list, file_name):
    #f = open(file_name, 'w')
    f = codecs.open(file_name, 'w', 'utf-8')
    the_str = json.dumps(dict_or_list)
    #the_str = the_str.encode('utf-8')
    f.write(the_str)
    f.close()

def readdir_and_writeback(dir_name):
    if not os.path.isdir(dir_name):
        print '%s is not a directory!' % dir_name
        sys.exit(1)
    #output_f = open(output_file_name, 'w')
    parser = TaxonomyParser()
    id2url_dict = {}
    for file_name in os.listdir(dir_name):
        if file_name.startswith('.'):
            continue
        file_name = dir_name + '/' + file_name
        print 'Parsing file %s ...' % file_name
        post = read_json_file(file_name)
        parser.set_post(post)
        categories = parser.parse_categories()
        companies = parser.parse_companies()
        locations = parser.parse_locations()
        if post.has_key('category'):
            del post['category']
        if categories:
            post['category'] = ','.join(categories)
        if post.has_key('company'):
            del post['company']
        #if companies:
        #    post['company'] = ','.join(companies)
        if post.has_key('location'):
            del post['location']
        #if locations:
        #    post['location'] = ','.join(locations)
        write_json_file(post, file_name)
    #output_f.close()


if __name__ == '__main__':
    #parser = TaxonomyParser()
    #post = {'title':'HTML5移动产品设计与开发', 'content':'「拔丝活动系列沙龙」是由拔丝活动（buzz.cn）联合各地社区组织主办的技术交流活动。内容涵盖技术、设计、互联网、软硬件等多个领域。主办地涉及北上广深、杭州、南京、成都、天津等地。主讲人均为目前战斗在一线的行业牛人。我们力争将第一手实践经验、干货与大家分享。 此次深圳沙龙由UC优视的张云龙和腾讯公司的何骏为大家带来「HTML5移动产品设计与开发」主题。由珠三角技术沙龙和腾讯大讲堂共同组织', 'address':'深圳市南山区科技中一路 腾讯大厦 二楼多功能厅'}
    #parser.set_post(post)
    #print parser.parse_categories()[0].decode('utf-8')

    if len(sys.argv) < 2:
        print_help(sys.argv)
        sys.exit(-1)
    input_dir = sys.argv[1]
    #output_file_name = sys.argv[2]
    reload(sys)
    sys.setdefaultencoding('utf-8')

    readdir_and_writeback(input_dir)
