
        function addPersonToCart (uuid) {
            var ret = $.get(
                '/cart/add/' + uuid + '/',
                function(data){
                    showMessage(data);
                    refreshCartContents();
                }
            );
        }

        function removePersonFromCart(uuid){
            var ret = $.get(
                '/cart/remove/' + uuid + '/',
                function(data){
                    refreshCartContents();
                }
            );
        }

        function emptyCart() {
            var ret = $.get(
                '/cart/empty/',
                function(data){
                    refreshCartContents();
                }
            );
        }

        function refreshCartContents(){
            // Refresh div element
            $.get(
                '/cart/view/',
                function(data){
                    var cartModalDiv = $('#cartViewModalBody');
                    cartModalDiv.empty();
                    cartModalDiv.append(data);

                    $('a[name=removeFromCartAnchor]').click(function(event){
                        var uuid = event.target.getAttribute('personuuid');
                        removePerson(uuid);
                    });
                }
            );
            // Refresh count
            $.get(
                '/cart/count/',
                function(data){
                    var cartCountSpan = $('span[name="cartCount"]');
                    cartCountSpan.empty();
                    cartCountSpan.append(data);
                }
            );
        }

        function showMessage(msgText, spnId){
            if(!spnId)
                spnId = 'message'
            var msgSpan= $('#' + spnId);
            msgSpan.text(msgText);
        }

        function getCPRNumbersString()
        {
            var table = document.getElementById("cart-table-rows");
            var numbers="";

            for (var r = 0, n = table.rows.length; r < n; r++)
            {
                var val = table.rows[r].cells[1].innerHTML;
                numbers+= val + ',';
            }
            if(numbers.length > 0)
                numbers = numbers.substr(0,numbers.length -1);
            return numbers;
        }

        function copyCprNumbers(){
            var numbers = getCPRNumbersString();
            window.clipboardData.setData('text', numbers);
            var txt = $('#spnCprNumbersCopied')
            msg = txt.attr('txt');
            showMessage(msg,'cartMessage');
        }

        function selectCPRNumbers(){
            var numbers = getCPRNumbersString();
            document.getElementById('copy-message').style.display = "block";
            document.getElementById('cart-nums').innerHTML = numbers;
            document.getElementById('cart-nums').style.display = "block";

            if (typeof window.getSelection != "undefined" && typeof document.createRange != "undefined"){
                var range = document.createRange();
                range.selectNodeContents(document.getElementById('cart-nums'));
                var sel = window.getSelection();
                sel.removeAllRanges();
                sel.addRange(range);
            }
            else if (typeof document.selection != "undefined" && typeof document.body.createTextRange != "undefined"){
                var textRange = document.body.createTextRange();
                textRange.moveToElementText(document.getElementById('cart-nums'));
                textRange.select();
            }
        }

        function showParents(event, uuid)
        {
            var parentsAnchor = event.target;
            var parentsDivId = '#parents-row'+uuid;
            var parentsDiv = $(parentsDivId);

            if(parentsDiv[0].style.display === 'block' || parentsDiv[0].style.display == '')
            {
                parentsDiv.slideUp("fast");
            }
            else // Show
            {
                if(parentsAnchor.attributes.loaded.value == 'false')
                {
                    $.post('/search/updateparents/'+uuid+'/', {}, function (data) {
                        if(data == "none")
                        {
                            window.alert("Not available");
                        }
                        else
                        {
                            parentsAnchor.attributes.loaded.value  = 'true';
                            parentsDiv.empty();
                            parentsDiv.append(data);
                            parentsDiv.slideDown("fast");
                        }
                    });
                }
                else
                {
                    parentsDiv.slideDown("fast");
                }
            }
        }