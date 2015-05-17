package util;

import play.mvc.Http.*;

/**
 * Created by Beemen on 17/5/15.
 */
public class DisplayState {
    public Boolean openCart;
    public String openPerson;
    public String openParents;

    private DisplayState(){

    }

    public static DisplayState fromSession(){
        Context ctx = Context.current();
        DisplayState ret = new DisplayState();

        if(ctx.session().containsKey("opencart")){
            ret.openCart = Boolean.parseBoolean(ctx.session().get("opencart"));
        }

        if(ctx.session().containsKey("showperson")){
            ret.openPerson = ctx.session().get("showperson");
        }

        if(ctx.session().containsKey("openparents")){
            ret.openParents = ctx.session().get("openparents");
        }

        return ret;
    }

    public void saveToSession(){
        Context ctx = Context.current();
         ctx.session().put("opencart", openCart.toString());
         ctx.session().put("showperson", openPerson );
         ctx.session().put("openparents", openParents );
    }
}
