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
import components.LocationIndex;
import components.MechModifier;
import components.SimplePlaceable;

public class stCockpitISSmall implements ifCockpit, ifState {
    private final static AvailableCode AC = new AvailableCode( AvailableCode.TECH_INNER_SPHERE );
    private SimplePlaceable Sensors = new SimplePlaceable( "Sensors", "Sensors", "Sensors", "Sensors", "Tech Manual", 1, true, AC );
    private SimplePlaceable LifeSupport = new SimplePlaceable( "Life Support", "Life Support", "Life Support", "Life Support", "Tech Manual", 1, true, AC );
    private SimplePlaceable SecondSensors = new SimplePlaceable( "Sensors", "Sensors", "Sensors", "Sensors", "Tech Manual", 1, true, AC );
    private SimplePlaceable SecondLifeSupport = new SimplePlaceable( "Life Support", "Life Support", "Life Support", "Life Support", "Tech Manual", 1, true, AC );
    private final MechModifier MechMod = new MechModifier( 0, 0, 0, 0.0f, 1, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, true, false );

    public stCockpitISSmall() {
        AC.SetISCodes( 'E', 'X', 'X', 'E' );
        AC.SetISDates( 0, 0, false, 3067, 0, 0, false, false );
        AC.SetISFactions( "", "", "FS", "" );
        AC.SetRulesLevels( AvailableCode.RULES_TOURNAMENT, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED, AvailableCode.RULES_UNALLOWED );
    }

    public boolean HasCounterpart() {
        return false;
    }

    public double GetTonnage() {
        double result = 2.0f;
        result += Sensors.GetTonnage();
        result += SecondSensors.GetTonnage();
        result += LifeSupport.GetTonnage();
        return result;
    }

    public boolean HasSecondLSLoc() {
        return false;
    }

    public SimplePlaceable GetLifeSupport() {
        return LifeSupport;
    }
    
    public SimplePlaceable GetSensors() {
        return Sensors;
    }

    public SimplePlaceable GetSecondLifeSupport() {
        return SecondLifeSupport;
    }
    
    public SimplePlaceable GetSecondSensors() {
        return SecondSensors;
    }

    public String ActualName() {
        return "Small Cockpit";
    }

    public String CritName() {
        return "Small Cockpit";
    }

    public String LookupName() {
        return "Small Cockpit";
    }

    public String ChatName() {
        return "Sml Cockpit";
    }

    public String MegaMekName( boolean UseRear ) {
        return "Cockpit";
    }

    public String BookReference() {
        return "Tech Manual";
    }

    public String GetReportName() {
        return "Small";
    }

    public double GetCost( int Tonnage, int year ) {
        double result = 225000.0f + ( 2000.0f * Tonnage );
        result += Sensors.GetCost();
        result += LifeSupport.GetCost();
        result += SecondSensors.GetCost();
        result += SecondLifeSupport.GetCost();
        return result;
    }
    
    public boolean HasFireControl() {
        return true;
    }

    public double BVMod() {
        return 0.95f;
    }

    public AvailableCode GetAvailability() {
        return AC;
    }

    public int ReportCrits() {
        return 4;
    }

    public MechModifier GetMechModifier() {
        return MechMod;
    }

    @Override
    public String toString() {
        return "Small Cockpit";
    }

    public LocationIndex GetCockpitLoc() {
        return new LocationIndex( 2, LocationIndex.MECH_LOC_HD, -1 );
    }

    public LocationIndex GetFirstSensorLoc() {
        return new LocationIndex( 1, LocationIndex.MECH_LOC_HD, -1 );
    }

    public LocationIndex GetSecondSensorLoc() {
        return new LocationIndex( 3, LocationIndex.MECH_LOC_HD, -1 );
    }

    public LocationIndex GetFirstLSLoc() {
        return new LocationIndex( 0, LocationIndex.MECH_LOC_HD, -1 );
    }

    public LocationIndex GetSecondLSLoc() {
        return null;
    }

    public boolean CanUseCommandConsole() {
        return true;
    }

    public boolean HasThirdSensors() {
        return false;
    }

    public LocationIndex GetThirdSensorLoc() {
        return null;
    }

    public SimplePlaceable GetThirdSensors() {
        return null;
    }

    public boolean IsTorsoMounted() {
        return false;
    }
}
