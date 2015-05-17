
      // Popup window code
      function newPopup(url) 
      {
        var w = 800;
        var h = 480;
        var left = (screen.width/2)-(w/2);
        var top = (screen.height/2)-(h/2);

        popupWindow = window.open(
          url,'Cart','resizable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,directories=no,status=no, width='+w+', height='+h+', top='+top+', left='+left);
      }

      function selectCPRNumbers()
      { 
        document.getElementById('copy-message').style.display = "block";

        var table = document.getElementById("cart-table-rows");
        var numbers="";
       
        for (var r = 0, n = table.rows.length; r < n; r++)
        {
            numbers+=table.rows[r].cells[1].innerHTML
            numbers+="<br>";
        }
        numbers+="<br>";

        document.getElementById('cart-nums').innerHTML = numbers;
        document.getElementById('cart-nums').style.display = "block";
 
        if (typeof window.getSelection != "undefined" && typeof document.createRange != "undefined") {
            var range = document.createRange();
            range.selectNodeContents(document.getElementById('cart-nums'));
            var sel = window.getSelection();
            sel.removeAllRanges();
            sel.addRange(range);
        } else if (typeof document.selection != "undefined" && typeof document.body.createTextRange != "undefined") {
            var textRange = document.body.createTextRange();
            textRange.moveToElementText(document.getElementById('cart-nums'));
            textRange.select();
        }
      }
