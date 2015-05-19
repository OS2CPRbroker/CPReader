
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
            var htmlRet = $.get(
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
        }

        function showMessage(msgText){
            var msgSpan= $('#message');
            msgSpan.text(msgText);
        }
