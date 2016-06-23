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

namespace util.cprbroker
{

    public interface IPersonRelationships
    {

        /* Relations
        List<PersonRelationType> p1 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getAegtefaelle();
        List<PersonFlerRelationType> p2 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getBoern();
        List<PersonFlerRelationType> p3 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getBopaelssamling();
        List<PersonRelationType> p4 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getErstatningAf();
        List<PersonFlerRelationType> p5 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getErstatningFor();
        List<PersonRelationType> p6 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getFader();
        List<PersonFlerRelationType> p7 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getForaeldremyndighedsboern();
        List<PersonRelationType> p8 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getForaeldremyndighedsindehaver();
        LokalUdvidelseType p9 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getLokalUdvidelse();
        List<PersonRelationType> p10 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getModer();
        List<PersonRelationType> p11 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getRegistreretPartner();
        List<PersonRelationType> p12 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getRetligHandleevneVaergeForPersonen();
        List<PersonFlerRelationType> p13 = laesOutput.getLaesResultat().getRegistrering().getRelationListe().getRetligHandleevneVaergemaalsindehaver();
        */
        int numberOfRelations();
        List<IRelationship> aegtefaelle();
        List<IRelationship> boern();
        List<IRelationship> bopaelssamling();
        List<IRelationship> erstatingAf();
        List<IRelationship> erstatingFor();
        List<IRelationship> fader();
        List<IRelationship> foraeldremydighedsboern();
        List<IRelationship> foraeldremyndighedsindehaver();
        List<IRelationship> moder();
        List<IRelationship> registreretPartner();
        List<IRelationship> retligHandleevneVaergeForPersonen();
        List<IRelationship> retligHandleevneVaergemaalsindehaver();
        List<IRelationship> getRelationshipsOfType(ERelationshipType relationshipType);


    }
}