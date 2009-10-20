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

import common.CommonTools;
import components.*;

public class stEngineCLFI implements ifEngine, ifState {
    // A Clan Fission Engine
    private final static AvailableCode AC = new AvailableCode( AvailableCode.TECH_CLAN );
    private final static float[] Masses = {5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,
        5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.0f,5.5f,5.5f,5.5f,6.5f,6.5f,
        7.0f,7.0f,7.0f,8.0f,8.0f,9.0f,9.0f,10.0f,10.0f,10.5f,10.5f,10.5f,12.5f,
        12.5f,13.5f,13.5f,14.0f,15.0f,15.0f,16.0f,17.0f,17.5f,17.5f,18.5f,19.5f,
        20.5f,21.0f,22.0f,23.0f,24.0f,24.5f,25.5f,27.5f,28.0f,29.0f,31.0f,31.5f,
        33.5f,34.5f,36.0f,38.0f,39.5f,41.5f,43.0f,45.0f,47.5f,50.0f,52.0f,55.5f,
        58.0f,60.5f,64.0f,67.5f,72.0f,76.5f,80.5f,86.0f,92.0f};

    public stEngineCLFI() {
        AC.SetCLCodes( 'D', 'X', 'E', 'D' );
        AC.SetCLDates( 0, 0, false, 1960, 0, 0, false, false );
        AC.SetCLFactions( "", "", "ES", "" );
        AC.SetRulesLevels( AvailableCode.RULES_ADVANCED, AvailableCode.RULES_TOURNAMENT, AvailableCode.RULES_ADVANCED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_ADVANCED );
    }

    public float GetTonnage( int Rating, boolean fractional ) {
        if( fractional ) {
            float retval = CommonTools.BaseEngineMass[GetIndex( Rating )] * 1.75f;
            if( retval < 5.0f ) { retval = 5.0f; }
            return retval;
        }
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

    public int GetCVSpace() {
        return 0;
    }

    public boolean CanSupportRating( int rate ) {
        if( rate < 5 || rate > 400 || rate % 5 != 0 ) {
            return false;
        } else {
            return true;
        }
    }

    public String GetLookupName() {
        return "Fission Engine";
    }

    public String GetCritName() {
        return "Fission Engine";
    }
    
    public String GetMMName() {
        return "Fission Engine";
    }

    public float GetCost( int MechTonnage, int Rating ) {
        return ( 7500 * MechTonnage * Rating ) / 75;
    }
    
    public AvailableCode GetAvailability() {
        return AC;
    }
    
    public int FreeHeatSinks() {
        return 5;
    }

    public float GetBVMult() {
        return 1.0f;
    }
    
    public boolean IsFusion() {
        return false;
    }

    public boolean IsNuclear() {
        return true;
    }

    public int GetFullCrits() {
        return 6;
    }

    private int GetIndex( int Rating ) {
        return Rating / 5 - 2;
    }
    
    public int MaxMovementHeat() {
        return 2;
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

    @Override
    public String toString() {
        return "Fission Engine";
    }
}
