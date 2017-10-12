<%@ page language="java"  contentType="text/html; charset=UTF-8" %>      <%--防止中文乱码--%>
<html>
<body>
<h2>Hello World!</h2>
springMVC上传文件
<form name="form1" action="/manage/product/upLoad.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc上传文件" />
</form>
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传富文本"/>
</form>
</body>
</html>
