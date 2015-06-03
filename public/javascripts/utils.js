
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


