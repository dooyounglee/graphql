# Issue

이슈 정리

|Date      |Issue|                         
|----------|------------------------------------------------------------|
|2022-10-05|start.spring.io에서 web으로 GraphQL을 추가했는데..  Header조작을 못해. HttpServletRequest는 dependencies에 없어|
|          |WebGraphQlRequest란게 있긴한데 spring security로 jwt 인증할라니까 filter를 사용해야 하는데|
|          |Filter가 WebGraphQlRequest로 되어 있겠냐구., HttpServletRequest로 되어 있지..|
|          |어쩔수없이 spring web도 추가함. 근데 이렇게 한 프로젝트에 여러 web이 있어도 되는건가??|
|----------|-----|
|2022-10-06|vo 하나에 EAGER가 2개면 서버가 안돌아가네? 근데 정말 필요한거면 어쩌지..  |
|          |암튼 signIn 했고, me(내정보 조회), jwt 이제 75퍼 이해한듯|
|          |session에서 가져오듯이, SecurityContextHolder에서 jwt에서 담았던 user 정보를 빼올수 있음. 어디서든. ~~session인줄~~|
|          |내일은 newNote, updateNote, deleteNote, addFavorite, deleteFavorite 다 할 수 있겠찌?|
|          ||
