$( document ).ready(function() {
    $('button.follow-button').on('click', function(){
        $button = $(this);
        if($button.hasClass('following')){

            var userId = $button.attr('rel');
            console.log("unfollow user["+ userId+"]");
            $.ajax({
                type: "POST",
                url: "/unfollow",
                // The key needs to match your method's input parameter (case-sensitive).
                data: JSON.stringify({ "unfollow": userId }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){console.log(data);},
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
            $button.removeClass('following');
            $button.removeClass('unfollow');
            $button.removeClass('btn-danger');
            $button.removeClass('btn-primary');
            $button.text('Follow');
        } else {

            var userId = $button.attr('rel');
            console.log("follow user["+ userId+"]");
            $.ajax({
                type: "POST",
                url: "/follow",
                // The key needs to match your method's input parameter (case-sensitive).
                data: JSON.stringify({ "follow": userId }),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){console.log(data);},
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });

            $button.addClass('following');
            $button.addClass('btn-primary');
            $button.text('Following');
        }
    });

    $('button.follow-button').hover(function(){
        $button = $(this);
        if($button.hasClass('following')){
            $button.addClass('unfollow');
            $button.removeClass('btn-primary')
            $button.addClass('btn-danger');
            $button.text('Unfollow');
        }
    }, function(){
        if($button.hasClass('following')){
            $button.removeClass('unfollow');
            $button.removeClass('btn-danger');
            $button.addClass('btn-primary');
            $button.text('Following');
        }
    });



    var tweetForm = $('#tweetForm');
    tweetForm.submit(function (ev) {
        var tweetContent = $('#tweet-content').val();
        $.ajax({
            type: tweetForm.attr('method'),
            url: tweetForm.attr('action'),
            data: JSON.stringify({ "content": tweetContent }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.log('Create tweet response: ' + data);
            }
        });
        ev.preventDefault();
    });
});