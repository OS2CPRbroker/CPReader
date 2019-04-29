
    define(["validate"], function (v) {
        return {
            processQuery: function () {

                var queryField = $('#query');
                var addressQueryField = $('#addressQuery');

                var query = queryField.val().trim();
                var addressQuery = addressQueryField.val().trim();
                var online = ($("input[name=online]:checked").val() === "true");

                var validationDoneCallBack = function(){
                    var valid = !queryField.parent().hasClass('has-error') && !queryField.parent().hasClass('has-warning');
                    if(!addressQueryField.attr('disabled') || !addressQueryField.attr('disabled') === 'false'){
                        valid &= !addressQueryField.parent().hasClass('has-error') && !addressQueryField.parent().hasClass('has-warning');
                    }

                    if (!valid)
                    {
                        return;
                    }


                    var cpr = /\b[0-9]{9,10}$/; // a cpr consists of exactly 10 numbers
                    var cprpattern = /[0-9]{5,6}-[0-9]{4}$/; // or 10 numbers with a - between 6th and 7th char
                    var lastname = /.*$/; // no commas
                    var firstlastname = /.*,\s*.*$/; // one comma
                    var firstmiddlelastname = /.*,\s*.*,\s.*$/; // two commas
                    var guidPattern = /[0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12}/i;

                    var redirectLocation = null;
                    var queryExists = false;

                    // if the input is recognised as a CPR number, redirect to a uuid search
                    if (cpr.test(query) || cprpattern.test(query)) {
                        query = query.replace("-", "");
                        if(query.length === 9)
                            query = "0" + query;
                        $.post('/search/cpr/', { "query": query }, function (data) {
                            var guidRegexMatch = guidPattern.test(data);
                            if (guidRegexMatch) {
                                window.location = '/show/uuid/' + data + '/';
                            } else {
                                window.location = '/search/error/' + data + '/';
                            }
                            
                        });
                    }
                    // otherwise build the name based search string and redirect to a name search
                    else if (firstmiddlelastname.test(query) || firstlastname.test(query) || lastname.test(query)) {
                        redirectLocation = '/search'
                        if (query.length > 0) {
                            queryExists = true;
                            redirectLocation += '/name/' + query;
                        }
                        if (addressQuery.length > 0) {
                            queryExists = true;
                            redirectLocation += '/address/' + addressQuery;
                        }
                        if (online)
                            redirectLocation += '/online'
                        redirectLocation += '/page/1';
                    }

                    else {
                        alert('error');
                    }

                    if (queryExists) {
                        window.location = redirectLocation;
                    }
                    else{
                        //alert('no query');
                    }
                };

                //Call made this way to force waiting for DAWA validation (if relevant)
                v.validateQueries(validationDoneCallBack);
            }
        }
    });
