
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

        function showMessage(msgText){
            var msgSpan= $('#message');
            msgSpan.text(msgText);
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