using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace cpreader.app.util
{
    public static class Constants
    {
        public static readonly String UuidRegex = @"{?[\d\w]{8}\-[\d\w]{4}\-[\d\w]{4}\-[\d\w]{4}\-[\d\w]{12}}?";

        public static readonly int SecondsInADay = 86400;

        public static readonly String DateRegex = @"(19|20)\d\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])";
    }
}