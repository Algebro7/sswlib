/*
Copyright (c) 2008~2009, Justin R. Bengtson (poopshotgun@yahoo.com)
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
        this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    * Neither the name of Justin R. Bengtson nor the names of contributors may
        be used to endorse or promote products derived from this software
        without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package states;

import components.AvailableCode;
import components.MechModifier;

public class stEnginePrimitiveFuelCell implements ifEngine, ifState {
    // An Inner Sphere Fuel Cell Engine
    private final static AvailableCode AC = new AvailableCode( AvailableCode.TECH_BOTH );
    private final static double[] Masses = {1.0f,1.0f,1.0f,1.0f,1.5f,1.5f,1.5f,
        1.5f,2.0f,2.0f,2.0f,2.5f,2.5f,2.5f,3.0f,3.0f,4.0f,4.0f,4.0f,4.5f,4.5f,
        5.0f,5.0f,5.0f,5.5f,5.5f,6.0f,6.0f,7.0f,7.0f,7.5f,7.5f,7.5f,8.5f,8.5f,
        9.0f,9.0f,10.0f,10.5f,10.5f,11.0f,11.5f,12.0f,12.0f,13.0f,13.5f,14.0f,
        14.5f,15.0f,16.0f,16.5f,17.0f,17.5f,19.0f,19.5f,20.0f,21.0f,22.0f,23.0f,
        23.5f,25.0f,26.0f,27.0f,28.5f,29.5f,31.0f,32.5f,34.5f,35.5f,38.0f,40.0f,
        41.5f,44.0f,46.5f,49.5f,52.5f,55.5f,59.0f,63.0f};
    private final static int[] BFStructure = {1,1,2,2,3,3,3,4,4,5,5,5,6,6,6,7,7,8,8};

    public stEnginePrimitiveFuelCell() {
        AC.SetISCodes( 'D', 'C', 'D', 'D' );
        AC.SetISDates( 0, 0, false, 1950, 0, 0, false, false );
        AC.SetISFactions( "", "", "PS", "" );
        AC.SetCLCodes( 'D', 'X', 'C', 'D' );
        AC.SetCLDates( 0, 0, false, 1950, 0, 0, false, false );
        AC.SetCLFactions( "", "", "PS", "" );
        AC.SetPIMAllowed( true );
        AC.SetPrimitiveOnly( true );
        AC.SetRulesLevels( AvailableCode.RULES_ADVANCED, AvailableCode.RULES_TOURNAMENT, AvailableCode.RULES_ADVANCED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );
    }

    public boolean HasCounterpart() {
        return false;
    }

    public double GetTonnage( int Rating ) {
        return Masses[GetIndex( Rating )];
    }

    public int GetCTCrits() {
        return 3;
    }

    public int GetSideTorsoCrits() {
        return 0;
    }

    public int NumCTBlocks() {
        return 2;
    }

    public boolean CanSupportRating( int rate ) {
        rate = (int) ( Math.floor( ( ( rate * 1.2f ) + 4.5f ) / 5 ) * 5 );
        if( rate < 5 || rate > 400 || rate % 5 != 0 ) {
            return false;
        } else {
            return true;
        }
    }

    public String ActualName() {
        return "Primitive Fuel-Cell Engine";
    }

    public String CritName() {
        return "Primitive Fuel-Cell";
    }

    public String LookupName() {
        return "Primitive Fuel-Cell Engine";
    }

    public String ChatName() {
        return "Pr FCE";
    }

    public String MegaMekName( boolean UseRear ) {
        return "Primitive Fuel-Cell";
    }

    public String BookReference() {
        return "Jihad Secrets: The Blake Documents";
    }

    public double GetCost( int MechTonnage, int Rating ) {
        return ( 3500.0f * ((double) MechTonnage) * ((double) Rating )) / 75.0f;
    }
    
    public AvailableCode GetAvailability() {
        return AC;
    }

    public int FreeHeatSinks() {
        return 1;
    }

    public double GetBVMult() {
        return 1.0f;
    }
    
    public boolean IsFusion() {
        return false;
    }

    public boolean IsNuclear() {
        return false;
    }

    public int GetFullCrits() {
        return 6;
    }

    private int GetIndex( int Rating ) {
        return Rating / 5 - 2;
    }

    private int GetBFIndex( int tonnage ) {
        return (tonnage - 10) / 5;
    }

    public int GetBFStructure( int tonnage ) {
        return BFStructure[GetBFIndex(tonnage)];
    }

    public int MaxMovementHeat() {
        return 0;
    }

    public int MinimumHeat() {
        return 0;
    }

    public int JumpingHeatMultiplier() {
        return 1;
    }

    public MechModifier GetMechModifier() {
        return null;
    }

    public boolean IsPrimitive() {
        return true;
    }

    @Override
    public String toString() {
        return "Primitive Fuel Cell Engine";
    }
}
