<!DOCTYPE html>
<html lang="en"
      class=" js flexbox canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths">
<!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style type="text/css"></style>
    <meta charset="utf-8">

    <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
	   Remove this if you use the .htaccess -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <title>AWS Demo</title>
    <meta name="description" content="">
    <meta name="author" content="">

    <!--  Mobile viewport optimized: j.mp/bplateviewport -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- CSS : implied media="all" -->
    <link rel="stylesheet" href="https://www.jbison.com/mrk_css/style.css?v=2">
    <link rel="stylesheet" href="https://www.jbison.com/mrk_css/basic.css">

    <link rel="stylesheet" href="https://www.jbison.com/mrk_css/desktop.css" media="screen and (min-width: 590px)">
    <link href="./jbison_files/css" rel="stylesheet" type="text/css">

    <!--[if IE 6]>
    <link rel="stylesheet" type="text/css" href="mrk_css/desktop.css"><![endif]-->
    <!--[if IE 7]>
    <link rel="stylesheet" type="text/css" href="mrk_css/desktop.css"><![endif]-->
    <!--[if IE 8]>
    <link rel="stylesheet" type="text/css" href="mrk_css/desktop.css"><![endif]-->

    <!-- All JavaScript at the bottom, except for Modernizr which enables HTML5 elements & feature detects -->
    <script type="text/javascript" async="" src="./jbison_files/ga.js"></script>
    <script src="./jbison_files/modernizr-1.6.min.js"></script>

    <script src="js/jquery-1.4.4.js" type="text/javascript"></script>
    <script src="js/pure-min-2.70.js" type="text/javascript"></script>

    <meta name="description" content="JBoss and web monitoring made easy!">
    <meta name="keywords" content="monitor,monitoring,jboss,web,jbison,bison,permgen,heap,memory">

</head>

<body>


<div id="_message_list_template" style="display: none">
    <div id="main">
        <table style="text-align: center; margin-left: auto; margin-right: auto; width: 80%; font-size: large">
            <tr>
                <th>Content</th>
                <th>Date</th>
                <th>Save Date</th>
                <th>Delay (in seconds)</th>
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


<div id="container">
    <div class="header_wrap">
        <header class="clearfix">
            <nav class="clearfix">
                <ul class="main_menu">
                    <li>Choose the room:</li>
                    <li><a href="#" id="_choose_room_confitura">Confitura</a></li>
                    <li><a href="#" id="_choose_room_robots">Robots</a></li>
                </ul>

                <div class="small_menu">
                    <span id="_add_message_flash"></span>
                </div>
            </nav>
        </header>
    </div>

    <div id="main">
        <h2 class="tophl banner">
            <div>
                Current room: <span id="_current_room">confitura</span>
            </div>

            <div>
                <form id="_add_message_form">
                    Message:
                    <input type="text" id="_add_message_content" maxlength="512"/>
                    <input type="submit" value="Add"/>
                </form>
            </div>

            <div>
                <form>
                    <input id="_message_list_refresh" value="Refresh" type="submit"/>
                </form>
            </div>
        </h2>

        <div class="main_in_wrapper">
            <div class="main_in homepage_main">

                <div id="_message_list_container">

                </div>

            </div>
        </div>
        <!--! end of .main_in_wrapper -->
    </div>
</div>
<!--! end of #container -->

<footer class="clearfix">
    <div class="footer_in">
        <div class="footer_left">
            This application is super cool !
        </div>


        <div class="footer_middle">
            <p><strong>(C) by 2011 <a href="http://softwaremill.eu/">SoftwareMill</a></strong></p>
        </div>
        <div class="footer_right">
            <ul>
            </ul>
        </div>
    </div>
</footer>

<script>
    $().ready(function() {
        var currentRoom = "confitura";

        function selectRoom(room) {
            currentRoom = room;
            $("#_message_list_container").empty();
            refreshMessageList();
            $("#_current_room").html(currentRoom);
        }

        $("#_choose_room_confitura").click(function(e) {
            e.preventDefault();
            selectRoom("confitura");
        });
        $("#_choose_room_robots").click(function(e) {
            e.preventDefault();
            selectRoom("robots");
        });

        $("#_add_message_form").submit(function(e) {
            e.preventDefault();
            var content = $("#_add_message_content").val();
            $("#_add_message_content").val("");

            if (content) {
                $.post("add_message", { content: content, room: currentRoom }, function() {
                    var $msg = $("<span>Message added, it will be visible shortly</span>");
                    $("#_add_message_flash").append($msg);
                    $msg.hide().fadeIn(1000).delay(2000).fadeOut(1000);
                });
            }
        });

        var fill_messages_list = $p("#_message_list_template div").compile({
                    "tr._message": {
                        "message<-messages": {
                            "td._message_content": "message.content",
                            "td._message_date": function(ctx) {
                                return formatDate(new Date(ctx.item.date));
                            },
                            "td._message_save_date": function(ctx) {
                                return formatDate(new Date(ctx.item.saveDate));
                            },
                            "td._message_delay": function(ctx) {
                                return (ctx.item.saveDate - ctx.item.date) / 1000;
                            }
                        }
                    }
                });

        function formatDate(date) {
            var year = date.getFullYear();
            var month = date.getMonth();
            var day = date.getDay();

            var hour = date.getHours();
            var minute = date.getMinutes();
            var seconds = date.getSeconds();

            return ""+year+"/"+pad(month, 2)+"/"+pad(day, 2)+" "+pad(hour, 2)+":"+pad(minute, 2)+":"+pad(seconds, 2);
        }

        function pad(number, length) {
            var str = '' + number;
            while (str.length < length) {
                str = '0' + str;
            }

            return str;
        }

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