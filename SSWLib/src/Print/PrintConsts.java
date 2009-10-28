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

package Print;

import java.awt.Font;

public class PrintConsts {
    public final static int MECHNAME = 0,
                            WALKMP = 1,
                            RUNMP = 2,
                            JUMPMP = 3,
                            TONNAGE = 4,
                            TECH_CLAN = 5,
                            TECH_IS = 6,
                            PILOT_NAME = 7,
                            PILOT_GUN = 8,
                            PILOT_PILOT = 9,
                            COST = 10,
                            BV2 = 11,
                            HEATSINK_NUMBER = 12,
                            HEATSINK_DISSIPATION = 13,
                            MAX_HEAT = 16,
                            TOTAL_ARMOR = 17,
                            STATS = 18;

    public final static String RS_TW_BP = "rs/RS_TW_BP.png",
                               RS_TW_QD = "rs/RS_TW_QD.png",
                               RS_TO_BP = "",
                               RS_TO_QD = "",
                               BP_ChartImage = "rs/Charts.png",
                               QD_ChartImage = "rs/ChartsQD.png",
                               BF_BG = "rs/BF_BG.png",
                               BF_IS = "rs/BF_BG.png",
                               BF_IS_Unit = "rs/BF_IS_Unit.png",
                               BF_CS = "rs/BF_BG.png",
                               BF_CS_Unit = "rs/BF_CS_Unit.png",
                               BF_CL = "rs/BF_BG.png",
                               BF_CL_Unit = "rs/BF_CL_Unit.png",
                               BF_Card = "rs/BF_Card.png",
                               BF_Chart = "rs/BF_Chart.png";

    public final static Font TitleFont = new Font( "Verdana", Font.BOLD, 12 );
    public final static Font BoldFont = new Font( "Arial", Font.BOLD, 8 );
    public final static Font PlainFont = new Font( "Arial", Font.PLAIN, 8 );
    public final static Font RegularFont = new Font( "Arial", Font.PLAIN, 10 );
    public final static Font Regular9Font = new Font( "Arial", Font.PLAIN, 9 );
    public final static Font ItalicFont = new Font( "Arial", Font.ITALIC, 8 );
    public final static Font SmallFont = new Font( "Arial", Font.PLAIN, 7 );
    public final static Font SmallItalicFont = new Font( "Arial", Font.ITALIC, 7 );
    public final static Font SmallBoldFont = new Font( "Arial", Font.BOLD, 7 );
    public final static Font ReallySmallFont = new Font( "Arial", Font.PLAIN, 6 );
    public final static Font XtraSmallBoldFont = new Font( "Arial", Font.BOLD, 6 );
    public final static Font XtraSmallFont = new Font( "Arial", Font.PLAIN, 6 );
}
