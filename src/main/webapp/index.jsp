<html>
<head>
    <script src="js/jquery-1.4.4.js" type="text/javascript"></script>
    <script src="js/pure-min-2.70.js" type="text/javascript"></script>
</head>
<body>
<div id="_message_list_template" style="display: none">
    <div>
        <table>
            <tr>
                <th>Content</th>
                <th>Date</th>
                <th>Save Date</th>
                <th>Delay</th>
            </tr>
            <tr class="_message">
                <td class="_message_content"></td>
                <td class="_message_date"></td>
                <td class="_message_save_date"></td>
                <td class="_message_delay"></td>
            </tr>
        </table>
    </div>
</div>

<div>
    Rooms:
    <a href="#" id="_choose_room_confitura">Confitura</a> |
    <a href="#" id="_choose_room_robots">Robots</a>
</div>

<div>
    Current room: <span id="_current_room">confitura</span>
</div>

<div>
    <form id="_add_message_form">
        Message:
        <input type="text" id="_add_message_content" maxlength="512" />
        <input type="submit" value="Add" />
        <span id="_add_message_flash"></span>
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
        var currentRoom = "confitura";

        function selectRoom(room) {
            currentRoom = room;
            $("#_message_list_container").empty();
            refreshMessageList();
            $("#_current_room").html(currentRoom);
        }

        $("#_choose_room_confitura").click(function(e) { e.preventDefault(); selectRoom("confitura"); });
        $("#_choose_room_robots").click(function(e) { e.preventDefault(); selectRoom("robots"); });

        $("#_add_message_form").submit(function(e) {
            e.preventDefault();
            var content = $("#_add_message_content").val();
            $("#_add_message_content").val("");

            if (content) {
                $.post("add_message", { content: content, room: currentRoom }, function() {
                    var $msg = $("<span>Message added, it will be visible shortly</span>");
                    $("#_add_message_flash").append($msg);
                    $msg.hide().fadeIn(2000).delay(5000).fadeOut(2000);
                });
            }
        });

        var fill_messages_list = $p("#_message_list_template div").compile({
                    "tr._message": {
                        "message<-messages": {
                            "td._message_content": "message.content",
                            "td._message_date": function(ctx) {
                                return new Date(ctx.item.date);
                            },
                            "td._message_save_date": function(ctx) {
                                return new Date(ctx.item.saveDate);
                            },
                            "td._message_delay": function(ctx) {
                                return (ctx.item.saveDate - ctx.item.date) / 1000;
                            }
                        }
                    }
                });

        function refreshMessageList() {
            $.get("list_messages", { room: currentRoom }, function(data) {
                var html = fill_messages_list({ messages: data });
                $("#_message_list_container").empty().html(html);
            });
        }

        $("#_message_list_refresh").click(function(e) {
            e.preventDefault();
            refreshMessageList();
        });

        // Referesh every 5secs
        window.setInterval(refreshMessageList, 500);

        // Initial refresh
        refreshMessageList();
    });
</script>
</body>
</html>
