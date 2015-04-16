function testCart()
      {
        
        $( "#cart-table-rows" ).append( "<p>Test</p>" );
        var url = "@{Cart.test}";
        window.alert(url);
        //window.location.replace(url);

 
      }

      function myFunction(a, b) {
          return a * b;
      }
      

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

      function removeFromCart(num)
      {
        window.alert(num);
        var table = document.getElementById("cart-table-rows");
        var exists = 0;

        for (var r = 0, n = table.rows.length; r < n; r++)
        {
            if(table.rows[r].cells[1].innerHTML == num)
            {
              exists++; 
            }
        }

        if (exists > 0)
        {
          // Create an empty <tr> element and add it to the 1st position of the table:
          table.deleteRow(exists);
        }
        selectCPRNumbers();
      }

      function emptyCart()
      {

        var table = document.getElementById("cart-table-rows");

        while(table.rows.length>0)
        {
          table.deleteRow(0);
        }
        selectCPRNumbers();
      }


      function addToCart(first, middle, last, num)
      {
        var table = document.getElementById("cart-table-rows");
        var exists = 0;

        for (var r = 0, n = table.rows.length; r < n; r++)
        {
            if(table.rows[r].cells[1].innerHTML == num)
            {
              exists++; 
            }
        }

        if (exists == 0)
        {
          // Create an empty <tr> element and add it to the 1st position of the table:
          var row = table.insertRow(0);

          // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
          var cell1 = row.insertCell(0);
          var cell2 = row.insertCell(1);
          var cell3 = row.insertCell(2);

          // Add some text to the new cells:
          cell1.innerHTML = first+" "+middle+" "+last;
          cell2.innerHTML = num;
          cell3.innerHTML = '<a href="#" OnClick="removeFromCart('+num+');" title="Remove">@Messages("cart.remove")</a>';

          updateCartContents();
        }
      }

      function updateCartContents()
      {
        var cartfunct = document.getElementById("cartfunctions");
        var cachecontents="";

        var table = document.getElementById("cart-table-rows");
        var exists = 0;

        for (var r = 0, n = table.rows.length; r < n; r++)
        {
            cachecontents+=table.rows[r].cells[1].innerHTML; 
        }

        //cartfunct.innerHTML = cachecontents;
        

        //cartfunct.innerHTML = '@session.put("cartcontents","")';

        window.alert(cachecontents);

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
