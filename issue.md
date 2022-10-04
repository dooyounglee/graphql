# Issue

이슈 정리

|Date      |Issue|                         
|----------|------------------------------------------------------------|
|2022-10-05|start.spring.io에서 web으로 GraphQL을 추가했는데..  Header조작을 못해. HttpServletRequest는 dependencies에 없어|
|          |WebGraphQlRequest란게 있긴한데 spring security로 jwt 인증할라니까 filter를 사용해야 하는데|
|          |Filter가 WebGraphQlRequest로 되어 있겠냐구., HttpServletRequest로 되어 있지..|
|          |어쩔수없이 spring web도 추가함. 근데 이렇게 한 프로젝트에 여러 web이 있어도 되는건가??|
