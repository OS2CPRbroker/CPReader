using System;
using System.Web;

namespace util
{

    /**
     * Created by Beemen on 17/5/15.
     * Modified by Mathias on 24/4/17
     */
    public class DisplayState
    {
        public Boolean openCart;
        public String openPerson;
        public String openParents;
        public String openFullpageview;

        private DisplayState()
        {

        }

        public static DisplayState fromSession()
        {
            var ctx = HttpContext.Current;

            DisplayState ret = new DisplayState();

            if (ctx.Session["opencart"] != null)
            {
                ret.openCart = Boolean.Parse(ctx.Session["opencart"].ToString());
            }

            if (ctx.Session["showperson"] != null)
            {
                ret.openPerson = ctx.Session["showperson"].ToString();
            }

            if (ctx.Session["openparents"] != null)
            {
                ret.openParents = ctx.Session["openparents"].ToString();
            }

            if (ctx.Session["showfullpageview"] != null)
            {
                ret.openFullpageview = ctx.Session["showfullpageview"].ToString();
            }

            return ret;
        }

        public void saveToSession()
        {
            var ctx = HttpContext.Current;
            ctx.Session["opencart"] = openCart.ToString();
            ctx.Session["showperson"] = openPerson;
            ctx.Session["openparents"] = openParents;
            ctx.Session["showfullpageview"] = openFullpageview;
        }
    }
}