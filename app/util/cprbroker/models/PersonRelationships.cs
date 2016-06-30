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









    public class PersonRelationships : IPersonRelationships
    {

        public int _numberOfRelations;
        public List<IRelationship> _aegtefaelle;
        public List<IRelationship> _boern;
        public List<IRelationship> _bopaelssamling;
        public List<IRelationship> _erstatingAf;
        public List<IRelationship> _erstatingFor;
        public List<IRelationship> _fader;
        public List<IRelationship> _foraeldremydighedsboern;
        public List<IRelationship> _foraeldremyndighedsindehaver;
        public List<IRelationship> _moder;
        public List<IRelationship> _registreretPartner;
        public List<IRelationship> _retligHandleevneVaergeForPersonen;
        public List<IRelationship> _retligHandleevneVaergemaalsindehaver;

        public class Builder
        {
            public int numberOfRelations = 0;
            public List<IRelationship> _aegtefaelle;
            public List<IRelationship> _boern;
            public List<IRelationship> _bopaelssamling;
            public List<IRelationship> _erstatingAf;
            public List<IRelationship> _erstatingFor;
            public List<IRelationship> _fader;
            public List<IRelationship> _foraeldremydighedsboern;
            public List<IRelationship> _foraeldremyndighedsindehaver;
            public List<IRelationship> _moder;
            public List<IRelationship> _registreretPartner;
            public List<IRelationship> _retligHandleevneVaergeForPersonen;
            public List<IRelationship> _retligHandleevneVaergemaalsindehaver;

            public IPersonRelationships build() { return new PersonRelationships(this); }

            public Builder selectTheRightRelationship(List<IRelationship> newRelationship)
            {

                // guard check
                if (newRelationship == null || newRelationship.Count == 0)
                {
                    return null;
                }

                ERelationshipType _type = newRelationship[0].relationshipType();

                switch (_type)
                {
                    case ERelationshipType.aegtefaelle: // spouse
                        aegtefaelle(newRelationship);
                        break;
                    case ERelationshipType.boern: // children
                        boern(newRelationship);
                        break;
                    case ERelationshipType.bopaelssamling:
                        bopaelssamling(newRelationship);
                        break;
                    case ERelationshipType.erstatingAf:
                        erstatingAf(newRelationship);
                        break;
                    case ERelationshipType.erstatingFor:
                        erstatingFor(newRelationship);
                        break;
                    case ERelationshipType.fader: // father
                        fader(newRelationship);
                        break;
                    case ERelationshipType.foraeldremydighedsboern:
                        foraeldremydighedsboern(newRelationship);
                        break;
                    case ERelationshipType.foraeldremyndighedsindehaver: // 
                        foraeldremyndighedsindehaver(newRelationship);
                        break;
                    case ERelationshipType.moder: // mother
                        moder(newRelationship);
                        break;
                    case ERelationshipType.registreretPartner: // registered partner
                        registreretPartner(newRelationship);
                        break;
                    case ERelationshipType.retligHandleevneVaergeForPersonen: // legal guardian
                        retligHandleevneVaergeForPersonen(newRelationship);
                        break;
                    case ERelationshipType.retligHandleevneVaergemaalsindehaver:
                        retligHandleevneVaergemaalsindehaver(newRelationship);
                        break;
                    default:
                        throw new ArgumentException(); //If ERelationshipType is modified it will be caught
                }
                return this;
            }

            public Builder aegtefaelle(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _aegtefaelle = defensiveCopyOfValues(newRelationship, ERelationshipType.aegtefaelle);
                return this;
            }

            public Builder boern(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _boern = defensiveCopyOfValues(newRelationship, ERelationshipType.boern);
                return this;
            }

            public Builder bopaelssamling(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _bopaelssamling = defensiveCopyOfValues(newRelationship, ERelationshipType.bopaelssamling);
                return this;
            }

            public Builder erstatingAf(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _erstatingAf = defensiveCopyOfValues(newRelationship, ERelationshipType.erstatingAf);
                return this;
            }

            public Builder erstatingFor(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _erstatingFor = defensiveCopyOfValues(newRelationship, ERelationshipType.erstatingFor);
                return this;
            }

            public Builder fader(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _fader = defensiveCopyOfValues(newRelationship, ERelationshipType.fader);
                return this;
            }

            public Builder foraeldremydighedsboern(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _foraeldremydighedsboern = defensiveCopyOfValues(newRelationship, ERelationshipType.foraeldremydighedsboern);
                return this;
            }

            public Builder foraeldremyndighedsindehaver(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _foraeldremyndighedsindehaver = defensiveCopyOfValues(newRelationship, ERelationshipType.foraeldremyndighedsindehaver);
                return this;
            }

            public Builder moder(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _moder = defensiveCopyOfValues(newRelationship, ERelationshipType.moder);
                return this;
            }

            public Builder registreretPartner(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _registreretPartner = defensiveCopyOfValues(newRelationship, ERelationshipType.registreretPartner);
                return this;
            }

            public Builder retligHandleevneVaergeForPersonen(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _retligHandleevneVaergeForPersonen = defensiveCopyOfValues(newRelationship, ERelationshipType.retligHandleevneVaergeForPersonen);
                return this;
            }

            public Builder retligHandleevneVaergemaalsindehaver(List<IRelationship> newRelationship)
            {
                numberOfRelations++;
                _retligHandleevneVaergemaalsindehaver = defensiveCopyOfValues(newRelationship, ERelationshipType.retligHandleevneVaergemaalsindehaver);
                return this;
            }

            /**
             * helper method to make the class immutable
             * @param referencedValues IRelationship representations of Relationships
             * @return  of a copy of the referencedValues
             */
            private List<IRelationship> defensiveCopyOfValues(
                                            List<IRelationship> referencedValues,
                                             ERelationshipType referencedType)
            {

                // null check
                if (referencedValues == null)
                {
                    return null;
                }

                // make defensive copy
                List<IRelationship> copy = new List<IRelationship>();

                foreach (IRelationship relationship in referencedValues)
                {
                    if (isCorrectRelationshipType(relationship, referencedType))
                    {
                        copy.Add(relationship);
                    }
                    else {
                        throw new ArgumentException();
                    }
                }

                return (copy);
            }

            /**
             * helper method to help check if the relationship is of the correct type
             * @param relationship IRelationship to be checked
             * @param shouldBeType ERelationshipType that the relationship parameter should be
             * @return Boolean value of the check if relationship is of shouldBeType
             */
            private Boolean isCorrectRelationshipType(IRelationship relationship, ERelationshipType shouldBeType)
            {
                if (relationship.relationshipType() == shouldBeType)
                {
                    return true;
                }
                return false;
            }

        }


        private PersonRelationships(Builder builder)
        {

            _erstatingAf = builder._erstatingAf;
            _erstatingFor = builder._erstatingFor;
            _fader = builder._fader;
            _moder = builder._moder;
            _foraeldremyndighedsindehaver = builder._foraeldremyndighedsindehaver;
            _retligHandleevneVaergeForPersonen = builder._retligHandleevneVaergeForPersonen;
            _aegtefaelle = builder._aegtefaelle;
            _registreretPartner = builder._registreretPartner;
            _boern = builder._boern;
            _foraeldremydighedsboern = builder._foraeldremydighedsboern;
            _retligHandleevneVaergemaalsindehaver = builder._retligHandleevneVaergemaalsindehaver;
            _bopaelssamling = builder._bopaelssamling;
            _numberOfRelations = builder.numberOfRelations;

        }


        public List<IRelationship> aegtefaelle() { return _aegtefaelle; }


        public List<IRelationship> boern() { return _boern; }


        public List<IRelationship> bopaelssamling() { return _bopaelssamling; }


        public List<IRelationship> erstatingAf() { return _erstatingAf; }


        public List<IRelationship> erstatingFor() { return _erstatingFor; }


        public List<IRelationship> fader() { return _fader; }


        public List<IRelationship> foraeldremydighedsboern() { return _foraeldremydighedsboern; }


        public List<IRelationship> foraeldremyndighedsindehaver() { return _foraeldremyndighedsindehaver; }


        public List<IRelationship> moder() { return _moder; }


        public List<IRelationship> registreretPartner() { return _registreretPartner; }


        public List<IRelationship> retligHandleevneVaergeForPersonen() { return _retligHandleevneVaergeForPersonen; }


        public List<IRelationship> retligHandleevneVaergemaalsindehaver() { return _retligHandleevneVaergemaalsindehaver; }


        public int numberOfRelations() { return _numberOfRelations; }


        public List<IRelationship> getRelationshipsOfType(ERelationshipType relationshipType)
        {

            switch (relationshipType)
            {
                case ERelationshipType.aegtefaelle: return aegtefaelle();
                case ERelationshipType.boern: return boern();
                case ERelationshipType.bopaelssamling: return bopaelssamling();
                case ERelationshipType.erstatingAf: return erstatingAf();
                case ERelationshipType.erstatingFor: return erstatingFor();
                case ERelationshipType.fader: return fader();
                case ERelationshipType.foraeldremydighedsboern: return foraeldremydighedsboern();
                case ERelationshipType.foraeldremyndighedsindehaver: return foraeldremyndighedsindehaver();
                case ERelationshipType.moder: return moder();
                case ERelationshipType.registreretPartner: return registreretPartner();
                case ERelationshipType.retligHandleevneVaergeForPersonen: return retligHandleevneVaergeForPersonen();
                case ERelationshipType.retligHandleevneVaergemaalsindehaver: return retligHandleevneVaergemaalsindehaver();
                default: throw new ArgumentException(); //If ERelationshipType is modified it will be caught
            }

        }
    }

}