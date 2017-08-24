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

    public class Relationship : IRelationship
    {

        public String _comment;
        public String _referenceUrn;
        public String _referenceUuid;
        public IVirkning _effect;
        public ERelationshipType _type;

        public class Builder
        {

            public String _comment;
            public String _referenceUrn;
            public String _referenceUuid;
            public IVirkning _effect;
            public ERelationshipType _type;

            public IRelationship build() { return new Relationship(this); }

            public Builder comment(String newComment) { _comment = newComment; return this; }
            public Builder referenceUrn(String newUrn) { _referenceUrn = newUrn; return this; }
            public Builder referenceUuid(String newUuid) { _referenceUuid = newUuid; return this; }
            public Builder effect(IVirkning newEffect) { _effect = newEffect; return this; }
            public Builder type(ERelationshipType newType) { _type = newType; return this; }

        }

        private Relationship(Builder builder)
        {
            _comment = builder._comment;
            _referenceUrn = builder._referenceUrn;
            _referenceUuid = builder._referenceUuid;
            _effect = builder._effect;
            _type = builder._type;
        }

        public String comment() { return _comment; }

        public String referenceUrn() { return _referenceUrn; }

        public String referenceUuid() { return _referenceUuid; }

        public IVirkning effect() { return _effect; }

        public ERelationshipType relationshipType() { return _type; }

        public string relationshipTypeString() { return Description.GetDescription(_type); }

    }

}