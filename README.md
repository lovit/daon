# Daon 형태소 분석기
[![Build Status](https://travis-ci.org/rasoio/daon.svg?branch=master)](https://travis-ci.org/rasoio/daon)

말뭉치 기반 한글 형태소 분석기입니다.

##### 2017-09-07 기준 on iMac (27-inch, Late 2013)
- 96.5%의 정확률 (측정 : [EvaluateModel](daon-spark/src/main/scala/daon/spark/EvaluateModel.scala))
- 초당 처리량 : 4만 문장(85문자 10어절), 약 6.6MB 처리 (측정 : [AnalyzerPerfTest](daon-core/src/jmh/java/daon/core/perf/AnalyzerPerfTest.java))
- 11만문자 평균 처리속도 : 0.260 ms

![구성도](./intro.jpg)

# Daon Core Usage

[Daon Core](daon-core/README.md)

# Daon Manager Usage

[Daon Manager run on Docker](docker/README.md)

# Daon Spark Usage

[Daon Spark](daon-spark/README.md)

# Daon Elasticsearch Plugin Usage

[Daon Elasticsearch](daon-elasticsearch/README.md)


### 말뭉치
 
[Rouzeta](https://shleekr.github.io/)에서 사용 된 수정된 세종 코퍼스입니다.

[https://ithub.korean.go.kr/user/member/memberPdsReferenceManager.do](https://ithub.korean.go.kr/user/member/memberPdsReferenceManager.do)


### 참고 문헌

[1] 신준철, 옥철영 (2012). 기분석 부분 어절 사전을 활용한 한국어 형태소 분석기. 정보과학회논문지 : 소프트웨어 및 응용, 39(5), 415-424.

[2] [http://blog.mikemccandless.com/2010/12/using-finite-state-transducers-in.html](http://blog.mikemccandless.com/2010/12/using-finite-state-transducers-in.html)

[3] [https://shleekr.github.io/](https://shleekr.github.io/)

[4] [https://bitbucket.org/eunjeon/](https://bitbucket.org/eunjeon/)

[5] [https://github.com/google/sentencepiece](https://github.com/google/sentencepiece)