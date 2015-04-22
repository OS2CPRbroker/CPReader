/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * Beemen Beshara
 *
 * The code is currently governed by OS2 - Offentligt digitaliserings-
 * fællesskab / http://www.os2web.dk .
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Beemen on 14/11/2014.
 */
public class StringUtils {
    public static String TrimZerosOnLeft(String string) {
        if (string != null)
            return string.replaceAll("\\A0+", "");
        return string;
    }

    public static String format(String format, Object... args) {
        List<Object> args2 = new ArrayList<Object>();
        for (int i = 0; i < args.length; i++) {
            args2.add((args[i] == null) ? "" : args[i]);
        }
        return String.format(format, args2.toArray());
    }

    public static String join(CharSequence separator, Iterable<String> array) {
        String ret = "";
        Iterator<String> iterator = array.iterator();
        if (iterator.hasNext()) {
            ret = iterator.next();
            while (iterator.hasNext()){
                ret += separator + iterator.next();
            }
        }
        return ret;
    }
}
