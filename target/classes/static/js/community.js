/*
* 提交回复
* */
function commentTarget(targetId,type,content) {
    if (!content){
        alert("不能回复空内容~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code==200){
                // 回复成功，刷新页面
                window.location.reload();
            }else {
                if (response.code == 2003 ){
                    var isAccepted = confirm(response.message);
                    if (a=isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=ed649d9ab02352975f0c&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable",true);
                    }
                }else{
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}
function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    if (!content){
        alert("不能回复空内容~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": questionId,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            if (response.code==200){
               // 回复成功，刷新页面
               window.location.reload();
            }else {
                if (response.code == 2003 ){
                    var isAccepted = confirm(response.message);
                    if (a=isAccepted){
                        window.open("https://github.com/login/oauth/authorize?client_id=ed649d9ab02352975f0c&redirect_uri=http://localhost:8887/callback&scope=user&state=1")
                        window.localStorage.setItem("closable",true);
                    }
                }else{
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}
/*
* 回复评论
* */
function comment(e) {
    var commentId =  e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    commentTarget(commentId,2,content);
}
/*
* 展开二级评论
* */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-"+id);
/*    //展开二级评论
    comments.toggleClass("in");*/

    var collapse = e.getAttribute("data-collapse");
    if ( collapse ){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else {
        var subCommentContainer = $("#comment-"+id);
        if (subCommentContainer.children().length !=1 ){
            comments.addClass("in");
            e.classList.add("active")
            //标记二级评论展开状态
            e.setAttribute("data-collapse","in");
        }else {
            $.getJSON("/comment/"+id,function (data) {
                console.log(data);
                $.each(data.data.reverse(),function (index,comment) {
                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>",{
                        "html":comment.content
                    })).append($("<div/>"),{
                        "class":"menu"
                    }).append($("<span/>",{
                        "class":"pull-right",
                        "html":moment.format
                    }));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>",{
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                })
            });
            comments.addClass("in");
            e.classList.add("active")
            //标记二级评论展开状态
            e.setAttribute("data-collapse","in");
        }
    }
}
/*
* 点击标签
* */
function selectTag(value) {
    var previous = $("#tag").val();
    if (previous){
        var pre =  previous.split(",")
        if (!pre.includes(value)){
            $("#tag").val(previous+','+value);
        }
    }else {
        $("#tag").val(value);
    }

}
