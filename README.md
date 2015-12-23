# clj-bosonnlp

clj-bosonnlp是[Boson NLP](http://bosonnlp.com/)的clojure封装。

Boson NLP提供了中文自然语言处理中，包括：
+ 情感分析(Sentiment Analysis)
+ 实体识别(Name Entity Recognition)
+ 依存句法(Dependent parser)
+ 关键词提取(Keywords extraction)
+ 新闻分类(News classification)
+ 语义联想(Semantic Words Suggestion)
+ 分词和词性(Segmentation and Postag of Chinese)

更详细的信息，请参见Boson[官方文档](http://bosonnlp.com/dev/center)。

## Usage

使用Leiningin的，在`project.clj`文件中加入:

```clojure
[clj-bosonnlp "0.1.1"]
```

使用maven的，在`pom.xml`中加入:
```xml
<dependency>
  <groupId>clj-bosonnlp</groupId>
  <artifactId>clj-bosonnlp</artifactId>
  <version>0.1.1</version>
</dependency>
```

然后就可以在项目中使用了，下面的是在clojure repl中使用`clj-bosonnlp`的例子：

```clojure
user=> (use '[clj-bosonnlp.core])
nil  
;; initialize with you api-token
user=> (initialize "<your-api-token>")
"<you-api-token>"
;; sentiment example 
user=> (sentiment ["这个世界好复杂", "计算机是科学么"]
[[0.17128982245610536 0.8287101775438946] \
[0.3028239178178842 0.6971760821821158]]
user=> (suggest "粉丝" 20)
[[0.9999999999999997 "粉丝/n"] [0.4860246796131101 "脑残粉/n"] \
[0.47638025976400966 "听众/n"] [0.4574711603743687 "球迷/n"] \
[0.44279396622121586 "观众/n"] [0.4399638841304087 "喷子/n"] \
[0.4370675116868156 "乐迷/n"] [0.4365171009654033 "鳗鱼/n"] \
[0.4357353461210972 "水军/n"] [0.43320908113367257 "好友/n"] \
[0.4321432244549219 "歌迷/n"] [0.4218593870538608 "影迷/n"] \
[0.4179423555308083 "前辈/n"] [0.4142211812540118 "网民/n"] \
[0.40556773652629086 "参赛者/n"] [0.40544885221034965 "博友/n"] \
[0.3976491020591731 "公知/n"] [0.3971053944003027 "支持者/n"] \
[0.3864395283882839 "选手/n"] [0.38543008430007086 "歌手/n"]]
user=> (tag ["这个世界好复杂", "计算机是科学么"]
[{"tag" ["DT" "M" "NN" "AD" "VA"], "word" ["这" "个" "世界" "好" "复杂"]} \
{"tag" ["NN" "VC" "NN" "SP"], "word" ["计算机" "是" "科学" "么"]}]
;; news classify result
user=> (classify ["俄否决安理会谴责叙军战机空袭阿勒颇平民", \
"邓紫棋谈男友林宥嘉：我觉得我比他唱得好", "Facebook收购印度初创公司:"])
[5 4 8]
clj-bosonnlp.core=> (depparser "这个世界好复杂")
[{"head" [1 2 4 4 -1], "role" ["M" "NMOD" "SBJ" "VMOD" "ROOT"], \
"tag" ["DT" "M" "NN" "AD" "VA"], "word" ["这" "个" "世界" "好" "复杂"]}]
clj-bosonnlp.core=> (depparser ["这个世界好复杂", "计算机是科学么"]
[{"head" [1 2 4 4 -1], "role" ["M" "NMOD" "SBJ" "VMOD" "ROOT"], \
"tag" ["DT" "M" "NN" "AD" "VA"], "word" ["这" "个" "世界" "好" "复杂"]} \
{"head" [1 -1 1 1], "role" ["SBJ" "ROOT" "VMOD" "VMOD"], \
"tag" ["NN" "VC" "NN" "SP"], "word" ["计算机" "是" "科学" "么"]}]

;; document summary api example
clj-bosonnlp.core> (def content (str "腾讯科技讯（刘亚澜）10月22日消息，"
     "前优酷土豆技术副总裁黄冬已于日前正式加盟芒果TV，出任CTO一职。"
     "资料显示，黄冬历任土豆网技术副总裁、优酷土豆集团产品技术副总裁等职务，"
     "曾主持设计、运营过优酷土豆多个大型高容量产品和系统。"
     "此番加入芒果TV或与芒果TV计划自主研发智能硬件OS有关。"
     "今年3月，芒果TV对外公布其全平台日均独立用户突破3000万，日均VV突破1亿，"
     "但挥之不去的是业内对其技术能力能否匹配发展速度的质疑，"
     "亟须招揽技术人才提升整体技术能力。"
     "芒果TV是国内互联网电视七大牌照方之一，之前采取的是“封闭模式”与硬件厂商预装合作，"
     "而现在是“开放下载”+“厂商预装”。"
     "黄冬在加盟土豆网之前曾是国内FreeBSD（开源OS）社区发起者之一，"
     "是研究并使用开源OS的技术专家，离开优酷土豆集团后其加盟果壳电子，"
     "涉足智能硬件行业，将开源OS与硬件结合，创办魔豆智能路由器。"
     "未来黄冬可能会整合其在开源OS、智能硬件上的经验，结合芒果的牌照及资源优势，"
     "在智能硬件或OS领域发力。"
     "公开信息显示，芒果TV在今年6月对外宣布完成A轮5亿人民币融资，估值70亿。"
     "据芒果TV控股方芒果传媒的消息人士透露，芒果TV即将启动B轮融资。"))
#'clj-bosonnlp.core/content
clj-bosonnlp.core> (summary {"content" content} 0.1)
"腾讯科技讯（刘亚澜）10月22日消息，前优酷土豆技术副总裁黄冬已于日前正式加盟芒果TV，出任CTO一职。"
clj-bosonnlp.core> (summary {"content" content} 0.2)
"腾讯科技讯（刘亚澜）10月22日消息，前优酷土豆技术副总裁黄冬已于日前正式加盟芒果TV，出任CTO一职。
未来黄冬可能会整合其在开源OS、智能硬件上的经验，结合芒果的牌照及资源优势，在智能硬件或OS领域发力。
据芒果TV控股方芒果传媒的消息人士透露，芒果TV即将启动B轮融资。"
clj-bosonnlp.core> (summary {"content" content} 30 true)
"此番加入芒果TV或与芒果TV计划自主研发智能硬件OS有关。"
clj-bosonnlp.core> 
```


## License

Copyright © 2015 m00nlight

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
