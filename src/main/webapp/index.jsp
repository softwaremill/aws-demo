<html>
<head>
<script src="/js/jquery-1.4.4.js" type="text/javascript"></script>
<script src="/js/pure-min-2.70.js" type="text/javascript"></script>
</head>
<body>
<div id="_message_list_template" style="display: none">
    <div>
        <table>
            <tr>
                <th>Content</th>
                <th>Date</th>
            </tr>
            <tr class="_message">
                <td class="_message_content"></td>
                <td class="_message_date"></td>
            </tr>
        </table>
    </div>
</div>

<div>
    <form id="_add_message_form">
        Message:
        <input type="text" id="_add_message_content" maxlength="512" />
        <input type="submit" value="Add" />
    </form>
</div>

<div>
    <form>
        <input id="_message_list_refresh" value="Refresh" type="submit" />
    </form>
</div>

<div id="_message_list_container">

</div>

<script>
    $().ready(function() {
        $("#_add_message_form").submit(function(e) {
            e.preventDefault();
            var content = $("#_add_message_content").val();

            if (content) {
                $.post("/add_message", { content: content }, function() {
                    console.log("Message added");
                });
            }
        });

        var fill_messages_list = $p("#_message_list_template div").compile({
                "tr._message": {
                    "message<-messages": {
                        "td._message_content": "message.content",
                        "td._message_date": "message.date"
                    }
                }
           });

        function refreshMessageList() {
            $.get("/list_messages", {}, function(data) {
                var html = fill_messages_list({ messages: data });
                $("#_message_list_container").empty().html(html);
            });
        }

        $("#_message_list_refresh").click(function(e) {
            e.preventDefault();
            refreshMessageList();
        })

    });
</script>
</body>
</html>
