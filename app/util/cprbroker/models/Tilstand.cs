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
 * Søren Kirkegård
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

using System;
using System.Collections.Generic;
namespace util.cprbroker.models
{






    public class Tilstand : ITilstand
    {

        public EMaritalStatusType _civilStatusKode;
        public IVirkning _civilTilstandsVirkning;
        public ELifeStatusType _livStatusKode;
        public IVirkning _livTilstandsVirkning;

        //TODO Look at ITilstand - isGraeseIndikator? Implement it!
        public class Builder
        {
            public EMaritalStatusType _civilStatusKode;
            public IVirkning _civilTilstandsVirkning;
            public ELifeStatusType _livStatusKode;
            public IVirkning _livTilstandsVirkning;

            public ITilstand build() { return new Tilstand(this); }

            public Builder civilStatusKode(String newKode) { _civilStatusKode = (EMaritalStatusType)Enum.Parse(typeof(EMaritalStatusType), newKode, true); return this; }
            public Builder civilTilstandsVirkning(IVirkning virkning) { _civilTilstandsVirkning = virkning; return this; }
            public Builder livStatusKode(String newKode) { _livStatusKode = (ELifeStatusType)Enum.Parse(typeof(ELifeStatusType), newKode, true); return this; }
            public Builder livTilstandsVirkning(IVirkning virkning) { _livTilstandsVirkning = virkning; return this; }


        }

        private Tilstand(Builder builder)
        {
            _civilStatusKode = builder._civilStatusKode;
            _civilTilstandsVirkning = builder._civilTilstandsVirkning;
            _livStatusKode = builder._livStatusKode;
            _livTilstandsVirkning = builder._livTilstandsVirkning;
        }


        public EMaritalStatusType civilStatusKode() { return _civilStatusKode; }

        public string civilStatusString() { return Description.GetDescription(_civilStatusKode); }

        public IVirkning civilTilstandsVirkning() { return _civilTilstandsVirkning; }

        public ELifeStatusType livStatusKode() { return _livStatusKode; }

        public string livStatusString() { return Description.GetDescription(_livStatusKode); }

        public IVirkning livTilstandsVirkning() { return _livTilstandsVirkning; }

    }

}