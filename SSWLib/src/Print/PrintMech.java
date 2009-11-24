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

import common.*;
import components.*;
import filehandlers.FileCommon;
import filehandlers.ImageTracker;
import filehandlers.Media;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Vector;
import java.util.prefs.Preferences;

public class PrintMech implements Printable {
    public Mech CurMech;
    private Image MechImage = null,
                  LogoImage = null,
                  RecordSheet = null,
                  ChartImage = null;
    private boolean Advanced = false,
                    Charts = false,
                    PrintPilot = true,
                    UseA4Paper = false,
                    Canon = false,
                    TRO = false;
    private String PilotName = "",
                    currentAmmoFormat = "";
    private int Piloting = 5,
                Gunnery = 4,
                MiniConvRate = 1;
    private double BV = 0.0;
    private ifPrintPoints points = null;
    private Color Black = new Color( 0, 0, 0 ),
                  Grey = new Color( 128, 128, 128 );
    private Media media = new Media();
    private ImageTracker imageTracker;
    private Preferences Prefs = Preferences.userNodeForPackage("/ssw/gui/frmMain".getClass());

    // <editor-fold desc="Constructors">
    public PrintMech( Mech m, Image i, boolean adv, boolean A4, ImageTracker images) {
        CurMech = m;
        imageTracker = images;
        MechImage = imageTracker.getImage(m.GetSSWImage());
        Advanced = adv;
        BV = CommonTools.GetAdjustedBV(CurMech.GetCurrentBV(), Gunnery, Piloting);
        UseA4Paper = A4;
        GetRecordSheet(imageTracker);
    }

    public PrintMech( Mech m, ImageTracker images ) {
        this( m, null, false, false, images);
    }

    public PrintMech( Mech m, String Warrior, int Gun, int Pilot, ImageTracker images) {
        this( m, null, false, false, images);
        SetPilotData(Warrior, Gun, Pilot);
    }
    // </editor-fold>
    
    // <editor-fold desc="Settor Methods">
    public void SetPilotData( String pname, int pgun, int ppilot ) {
        PilotName = pname;
        Piloting = ppilot;
        Gunnery = pgun;
        setBV(CommonTools.GetAdjustedBV(BV, Gunnery, Piloting));
    }

    public void SetOptions( boolean charts, boolean PrintP, double UseBV ) {
        Charts = charts;
        setBV(UseBV);
        PrintPilot = PrintP;
    }

    public void SetMiniConversion( int conv ) {
        MiniConvRate = conv;
    }

    public void setMechwarrior(String name) {
        PilotName = name;
    }

    public void setGunnery(int gunnery) {
        Gunnery = gunnery;
    }

    public void setPiloting(int piloting) {
        Piloting = piloting;
    }

    public void setCharts(Boolean b) {
        Charts = b;
    }

    public void setPrintPilot(Boolean b) {
        PrintPilot = b;
    }

    public void setMechImage(Image MechImage) {
        if ( MechImage != null) { this.MechImage = MechImage; }
    }

    public void setLogoImage(Image LogoImage) {
        if ( LogoImage != null) { this.LogoImage = LogoImage; }
    }

    public void setBV(double BV) {
        this.BV = BV;
    }

    public void setCanon( boolean Canon ) {
        this.Canon = Canon;
    }

    public void setTRO(boolean TRO) {
        this.TRO = TRO;
        setCanon(true);
        setCharts(false);
        SetMiniConversion(1);
        setPrintPilot(false);
        currentAmmoFormat = Prefs.get( "AmmoNamePrintFormat", "" );
        Prefs.put( "AmmoNamePrintFormat", "Ammo (%P) %L" );
    }

    // </editor-fold>

    // <editor-fold desc="Gettor Methods">
    public String getMechwarrior(){
        return PilotName;
    }

    public int getGunnery(){
        return Gunnery;
    }

    public int getPiloting(){
        return Piloting;
    }
    
    public Image getMechImage() {
        return MechImage;
    }
    
    public Image getLogoImage() {
        return LogoImage;
    }

    public boolean isTRO() {
        return TRO;
    }
    // </editor-fold>

    public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) throws PrinterException {
        ((Graphics2D) graphics).translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
        if( RecordSheet == null ) {
            return Printable.NO_SUCH_PAGE;
        } else {
            PreparePrint( (Graphics2D) graphics );
            if ( !currentAmmoFormat.isEmpty() ) { Prefs.put( "AmmoNamePrintFormat", currentAmmoFormat); }
            return Printable.PAGE_EXISTS;
        }
    }
    
    private void PreparePrint( Graphics2D graphics ) {
        this.BV = CommonTools.GetAdjustedBV(CurMech.GetCurrentBV(), Gunnery, Piloting);

        // adjust the printable area for A4 paper size
        if( UseA4Paper ) {
            graphics.scale( 0.9705d, 0.9705d );
        }
        
        // adjust the printable area for use with helpful charts
        if( Charts ) {
            graphics.scale( 0.8d, 0.8d );
        }

        graphics.drawImage( RecordSheet, 0, 0, 576, 756, null );

        DrawPips( graphics );
        DrawCriticals( graphics );
        DrawMechData( graphics );
        DrawImages( graphics );

        if( Charts ) {
            // reset the scale and add the charts
            graphics.scale( 1.25d, 1.25d );
            graphics.drawImage( ChartImage, 0, 0, 576, 756, null );
            //AddCharts( graphics );
        }
        //DrawGrid( graphics );
    }

    private void DrawPips( Graphics2D graphics ) {
        PIPPrinter ap = new PIPPrinter(graphics, CurMech, Canon, imageTracker);
        ap.Render();
    }

    private void DrawCriticals( Graphics2D graphics ) {
        abPlaceable[] a = null;
        Point[] p = null;
        graphics.setFont( PrintConsts.SmallBoldFont );

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_HD );
        p = points.GetCritHDPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End = Current.NumCrits() + j;
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_CT );
        p = points.GetCritCTPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_CT] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LT );
        p = points.GetCritLTPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_LT] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics,GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RT );
        p = points.GetCritRTPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_RT] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LA );
        p = points.GetCritLAPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_LA] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RA );
        p = points.GetCritRAPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_RA] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_LL );
        p = points.GetCritLLPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_LL] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }

        a = CurMech.GetLoadout().GetCrits( LocationIndex.MECH_LOC_RL );
        p = points.GetCritRLPoints();
        for( int i = 0; i < a.length && i < p.length; i++ ) {
            if( a[i].NumCrits() > 1 && a[i].Contiguous() &! ( a[i] instanceof Engine ) &! ( a[i] instanceof Gyro ) ) {
                // print the multi-slot indicator before the item
                abPlaceable Current = a[i];
                int j = i;
                int End;
                if( Current.CanSplit() ) {
                    int[] check = CurMech.GetLoadout().FindInstances( Current );
                    End = check[LocationIndex.MECH_LOC_RL] + j;
                } else {
                    End = Current.NumCrits() + j;
                }
                if( End > a.length ) {
                    End = a.length - 1;
                }
                for( ; j < End; j++ ) {
                    if( j == i ) {
                        // starting out
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x + 2, p[j].y - 3 );
                        graphics.drawLine( p[j].x, p[j].y - 3, p[j].x, p[j].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else if( j == End - 1 ) {
                        // end the line
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x + 2, p[j].y - 2 );
                        graphics.drawLine( p[j].x, p[j].y - 2, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    } else {
                        // continue the line
                        graphics.drawLine( p[j].x, p[j].y, p[j].x, p[j-1].y );
                        if( a[j].IsArmored() ) {
                            graphics.drawOval( p[j].x + 3, p[j].y - 5, 5, 5 );
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 10, p[j].y );
                        } else {
                            graphics.drawString( GetPrintName( a[j] ), p[j].x + 3, p[j].y );
                        }
                    }
                }
                i = j - 1;
            } else {
                // single slot item
                if( ! a[i].IsCritable() ) {
                    DrawNonCritable( graphics, GetPrintName( a[i] ), p[i].x, p[i].y );
                } else {
                    if( a[i].IsArmored() ) {
                        graphics.drawOval( p[i].x, p[i].y - 5, 5, 5 );
                        graphics.drawString( GetPrintName( a[i] ), p[i].x + 7, p[i].y );
                    } else if( a[i] instanceof Ammunition ) {
                        graphics.drawString( FileCommon.FormatAmmoPrintName( (Ammunition) a[i], 1 ), p[i].x, p[i].y );
                    } else {
                        graphics.drawString( GetPrintName( a[i] ), p[i].x, p[i].y );
                    }
                }
            }
        }
    }

    private void DrawMechData( Graphics2D graphics ) {
        Point[] p = null;

        p = points.GetHeatSinkPoints();
        for( int i = 0; i < CurMech.GetHeatSinks().GetNumHS(); i++ ) {
            graphics.drawOval( p[i].x, p[i].y, 5, 5 );
        }

        PlaceableInfo[] a = SortEquipmentByLocation();
        p = points.GetWeaponChartPoints();
        graphics.setFont( PrintConsts.SmallFont );
        if (a.length >= 9) { graphics.setFont( PrintConsts.XtraSmallFont ); }
        int offset = 0;
        boolean PrintSpecials = false;
        for( int i = 0; i < a.length; i++ ) {
            PlaceableInfo item = a[i];
            graphics.drawString( item.Count + "", p[0].x, p[0].y + offset );
            graphics.drawString( GetPrintName( item.Item ), p[1].x, p[1].y + offset );
            graphics.drawString( FileCommon.EncodeLocation( item.Location, CurMech.IsQuad() ), p[2].x, p[2].y + offset );
            if( item.Item instanceof Equipment ) {
                graphics.drawString( ((Equipment) item.Item).GetHeat() + "", p[3].x, p[3].y + offset );
            } else if( item.Item instanceof ifWeapon ) {
                if( ((ifWeapon) item.Item).IsUltra() || ((ifWeapon) item.Item).IsRotary() ) {
                    graphics.drawString( ((ifWeapon) item.Item).GetHeat() + "/s", p[3].x, p[3].y + offset );
                } else {
                    graphics.drawString( ((ifWeapon) item.Item).GetHeat() + "", p[3].x, p[3].y + offset );
                }
            } else {
                graphics.drawString( "-", p[3].x, p[3].y + offset );
            }
            if( item.Item instanceof ifWeapon ) {
                if( ((ifWeapon) item.Item).GetWeaponClass() == ifWeapon.W_MISSILE ) {
                    graphics.drawString( ((ifWeapon) item.Item).GetDamageShort() + "/m", p[4].x, p[4].y + offset );
                    PrintSpecials = true;
                } else {
                    if( ((ifWeapon) item.Item).GetDamageShort() != ((ifWeapon) item.Item).GetDamageMedium() ||  ((ifWeapon) item.Item).GetDamageShort() != ((ifWeapon) item.Item).GetDamageLong() ||  ((ifWeapon) item.Item).GetDamageMedium() != ((ifWeapon) item.Item).GetDamageLong() ) {
                        graphics.drawString( ((ifWeapon) item.Item).GetDamageShort() + "/" + ((ifWeapon) item.Item).GetDamageMedium() + "/" + ((ifWeapon) item.Item).GetDamageLong(), p[4].x, p[4].y + offset );
                        PrintSpecials = true;
                    } else {
                        if( ((ifWeapon) item.Item).GetSpecials().equals( "-" ) ) {
                            graphics.drawString( ((ifWeapon) item.Item).GetDamageShort() + " [" + ((ifWeapon) item.Item).GetType() + "]", p[4].x, p[4].y + offset );
                            PrintSpecials = false;
                        } else {
                            graphics.drawString( ((ifWeapon) item.Item).GetDamageShort() + "", p[4].x, p[4].y + offset );
                            PrintSpecials = true;
                        }
                    }
                }
            } else {
                if( item.Item instanceof Equipment ) {
                    if( ((Equipment) item.Item).GetSpecials().equals( "-" ) ) {
                        graphics.drawString( "[" + ((Equipment) item.Item).GetType() + "]", p[4].x, p[4].y + offset );
                        PrintSpecials = false;
                    } else {
                        graphics.drawString( "-", p[4].x, p[4].y + offset );
                        PrintSpecials = true;
                    }
                } else {
                    graphics.drawString( "-", p[4].x, p[4].y + offset );
                    PrintSpecials = true;
                }
            }
            if( item.Item instanceof ifWeapon ) {
                if( ((ifWeapon) item.Item).GetRangeMin() < 1 ) {
                    graphics.drawString( "-", p[5].x, p[5].y + offset );
                } else {
                    graphics.drawString( (((ifWeapon) item.Item).GetRangeMin() * MiniConvRate ) + "", p[5].x, p[5].y + offset );
                }
            } else {
                graphics.drawString( "-", p[5].x, p[5].y + offset );
            }
            if( item.Item instanceof ifWeapon ) {
                graphics.drawString( (((ifWeapon) item.Item).GetRangeShort() * MiniConvRate ) + "", p[6].x, p[6].y + offset );
            } else if( item.Item instanceof Equipment ) {
                graphics.drawString( (((Equipment) item.Item).GetShortRange() * MiniConvRate ) + "", p[6].x, p[6].y + offset );
            } else {
                graphics.drawString( "-", p[6].x, p[6].y + offset );
            }
            if( item.Item instanceof ifWeapon ) {
                graphics.drawString( (((ifWeapon) item.Item).GetRangeMedium() * MiniConvRate ) + "", p[7].x, p[7].y + offset );
            } else if( item.Item instanceof Equipment ) {
                graphics.drawString( (((Equipment) item.Item).GetMediumRange() * MiniConvRate ) + "", p[7].x, p[7].y + offset );
            } else {
                graphics.drawString( "-", p[7].x, p[7].y + offset );
            }
            if( item.Item instanceof ifWeapon ) {
                graphics.drawString( (((ifWeapon) item.Item).GetRangeLong() * MiniConvRate ) + "", p[8].x, p[8].y + offset );
            } else if( item.Item instanceof Equipment ) {
                graphics.drawString( (((Equipment) item.Item).GetLongRange() * MiniConvRate ) + "", p[8].x, p[8].y + offset );
            } else {
                graphics.drawString( "-", p[8].x, p[8].y + offset );
            }

            offset += graphics.getFont().getSize();

            // check to see how if we need to print our special codes.
            if( PrintSpecials ) {
                String Codes = "";
                if( item.Item instanceof ifWeapon ) {
                    ifWeapon w = (ifWeapon) item.Item;
                    Codes = ("[" + w.GetType() + ", " + w.GetSpecials() + "]").replace(", -", "");
                } else if( item.Item instanceof Equipment ) {
                    Equipment e = (Equipment) item.Item;
                    Codes = ("[" + e.GetType() + ", " + e.GetSpecials() + "]").replace(", -", "");
                }
                graphics.drawString( Codes, p[1].x + 2, p[1].y + offset );
                offset += graphics.getFont().getSize();
            }
        }

        //HARD CODED CHECK FOR TC!! SHOULD BE REPLACED SOMETIME IN THE FUTURE!!!
        if (CurMech.GetLoadout().UsingTC()) {
            TargetingComputer tc = CurMech.GetLoadout().GetTC();
            graphics.drawString("1", p[0].x, p[0].y + offset);
            graphics.drawString(tc.CritName(), p[1].x, p[1].y + offset);
            offset += graphics.getFont().getSize();
        }
        offset += (graphics.getFont().getSize() * 2);

        //Output the list of Ammunition
        Vector AmmoList = GetAmmo();
        if ( AmmoList.size() > 0 ) {
            graphics.drawString("Ammunition Type", p[0].x, p[0].y + offset);
            graphics.drawString("Rounds", p[3].x, p[3].y + offset);
            offset += 2;
            graphics.drawLine(p[0].x, p[0].y + offset, p[8].x + 8, p[8].y + offset);
            offset += graphics.getFont().getSize();
        }
        for ( int index=0; index < AmmoList.size(); index++ ) {
            AmmoData CurAmmo = (AmmoData) AmmoList.get(index);
            graphics.drawString( CurAmmo.Format(), p[0].x, p[0].y + offset);
            graphics.drawString( CurAmmo.LotSize + "", p[3].x, p[3].y + offset);
            offset += graphics.getFont().getSize();
        }

        graphics.setFont( PrintConsts.BoldFont );
        p = points.GetDataChartPoints();
        graphics.drawString( CurMech.GetFullName(), p[PrintConsts.MECHNAME].x, p[PrintConsts.MECHNAME].y );

        // have to hack the movement to print the correct stuff here.
        graphics.setFont( PrintConsts.PlainFont );
        if( CurMech.GetAdjustedWalkingMP( false, true ) != CurMech.GetWalkingMP() ) {
            graphics.drawString( ( CurMech.GetWalkingMP() * MiniConvRate ) + " (" + ( CurMech.GetAdjustedWalkingMP( false, true ) * MiniConvRate ) + ")", p[PrintConsts.WALKMP].x, p[PrintConsts.WALKMP].y );
        } else {
            graphics.drawString( ( CurMech.GetWalkingMP() * MiniConvRate ) + "", p[PrintConsts.WALKMP].x, p[PrintConsts.WALKMP].y );
        }
        if( CurMech.GetAdjustedRunningMP( false, true ) != CurMech.GetRunningMP() ) {
            if( CurMech.GetAdjustedRunningMP( false, true ) < CurMech.GetRunningMP() ) {
                graphics.drawString( ( CurMech.GetAdjustedRunningMP( false, true ) * MiniConvRate ) + "", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
            } else {
                graphics.drawString( ( CurMech.GetRunningMP() * MiniConvRate ) + " (" + ( CurMech.GetAdjustedRunningMP( false, true ) * MiniConvRate ) + ")", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
            }
        } else {
            graphics.drawString( ( CurMech.GetRunningMP() * MiniConvRate ) + "", p[PrintConsts.RUNMP].x, p[PrintConsts.RUNMP].y );
        }
        if( CurMech.GetAdjustedJumpingMP( false ) != CurMech.GetJumpJets().GetNumJJ() ) {
            graphics.drawString( ( CurMech.GetJumpJets().GetNumJJ() * MiniConvRate ) + " (" + ( CurMech.GetAdjustedJumpingMP( false ) * MiniConvRate ) + ")", p[PrintConsts.JUMPMP].x, p[PrintConsts.JUMPMP].y );
        } else {
            graphics.drawString( ( CurMech.GetJumpJets().GetNumJJ() * MiniConvRate ) + "", p[PrintConsts.JUMPMP].x, p[PrintConsts.JUMPMP].y );
        }
        // end hacking of movement.

        //Tonnage
        graphics.drawString( CurMech.GetTonnage() + "", p[PrintConsts.TONNAGE].x, p[PrintConsts.TONNAGE].y );

        //Cost
        graphics.setFont( PrintConsts.Small8Font );
        graphics.drawString( String.format( "%1$,.0f C-Bills", Math.floor( CurMech.GetTotalCost() + 0.5f ) ), p[PrintConsts.COST].x, p[PrintConsts.COST].y );
        
        if ( !TRO ) {
            graphics.drawString( String.format( "%1$,.0f (Base: %2$,d)", BV, CurMech.GetCurrentBV() ), p[PrintConsts.BV2].x, p[PrintConsts.BV2].y );
            graphics.drawString( "Weapon Heat (" + CurMech.GetWeaponHeat() + ")", p[PrintConsts.MAX_HEAT].x, p[PrintConsts.MAX_HEAT].y );
            graphics.setFont( PrintConsts.SmallFont );
            graphics.drawString( "Armor Pts: " + CurMech.GetArmor().GetArmorValue(), p[PrintConsts.TOTAL_ARMOR].x, p[PrintConsts.TOTAL_ARMOR].y );
            graphics.setFont( PrintConsts.BoldFont );
        } else {
            graphics.drawString( String.format( "%1$,d", CurMech.GetCurrentBV() ), p[PrintConsts.BV2].x, p[PrintConsts.BV2].y );
        }

        graphics.setFont( PrintConsts.PlainFont );
        if ( TRO ) {
            graphics.setFont( PrintConsts.BoldFont );
            graphics.drawString( "____________________", p[PrintConsts.PILOT_NAME].x, p[PrintConsts.PILOT_NAME].y );
            graphics.drawString( "___", p[PrintConsts.PILOT_GUN].x, p[PrintConsts.PILOT_GUN].y);
            graphics.drawString( "___", p[PrintConsts.PILOT_PILOT].x-4, p[PrintConsts.PILOT_PILOT].y);
        } else if( PrintPilot ) {
            graphics.drawString( PilotName, p[PrintConsts.PILOT_NAME].x, p[PrintConsts.PILOT_NAME].y );
            graphics.drawString( Gunnery + "", p[PrintConsts.PILOT_GUN].x, p[PrintConsts.PILOT_GUN].y );
            graphics.drawString( Piloting + "", p[PrintConsts.PILOT_PILOT].x, p[PrintConsts.PILOT_PILOT].y );
        }

        // check boxes
        graphics.setFont( PrintConsts.PlainFont );
        String temp = CurMech.GetHeatSinks().LookupName();
        temp = temp.split( " " )[0];
        graphics.drawString( temp, p[PrintConsts.HEATSINK_NUMBER].x, p[PrintConsts.HEATSINK_NUMBER].y + 11 );

        temp = CommonTools.GetTechbaseString( CurMech.GetLoadout().GetTechBase() );
        graphics.drawString( temp, p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y );

        graphics.drawString( CurMech.GetYear() + "", p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y + 10 );

        if ( !TRO ) {
            //Armor Type
            graphics.setFont( PrintConsts.SmallFont );
            if ( CurMech.IsQuad() ) { graphics.setFont( PrintConsts.XtraSmallFont ); }

            int baseX = points.GetArmorInfoPoints()[LocationIndex.MECH_LOC_CT].x;
            int baseY = points.GetArmorInfoPoints()[LocationIndex.MECH_LOC_CT].y + 15;

            if ( CurMech.GetArmor().RequiresExtraRules() ) {
                graphics.setFont( PrintConsts.SmallBoldFont );
                if ( CurMech.IsQuad() ) { graphics.setFont( PrintConsts.XtraSmallBoldFont ); }
            }

            String[] parts = CurMech.GetArmor().CritName().trim().split(" ");
            for (String part: parts) {
                if ( !part.trim().isEmpty() ) {
                    int xCoord = baseX - ((part.trim().length() / 2) * 3);
                    graphics.drawString( part, xCoord, baseY );
                    baseY += 10;
                }
            }
            graphics.setFont( PrintConsts.PlainFont );

            //Availability Codes
            graphics.drawString(CurMech.GetAvailability().GetBestCombinedCode(), p[PrintConsts.TECH_IS].x, p[PrintConsts.TECH_IS].y+20);
        }

        //heat sinks
        graphics.setFont( PrintConsts.PlainFont );
        graphics.drawString( CurMech.GetHeatSinks().GetNumHS() + " (" + CurMech.GetHeatSinks().TotalDissipation() + ")", p[PrintConsts.HEATSINK_NUMBER].x, p[PrintConsts.HEATSINK_NUMBER].y );
        //graphics.drawString( CurMech.GetHeatSinks().TotalDissipation() + "", p[PrintConsts.HEATSINK_DISSIPATION].x, p[PrintConsts.HEATSINK_DISSIPATION].y );

        // internal information
        graphics.setFont( PrintConsts.SmallFont );
        p = points.GetInternalInfoPoints();
        graphics.drawString( "[" + CurMech.GetIntStruc().GetCTPoints() + "]", p[LocationIndex.MECH_LOC_CT].x, p[LocationIndex.MECH_LOC_CT].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetSidePoints() + "]", p[LocationIndex.MECH_LOC_LT].x, p[LocationIndex.MECH_LOC_LT].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetSidePoints() + "]", p[LocationIndex.MECH_LOC_RT].x, p[LocationIndex.MECH_LOC_RT].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetArmPoints() + "]", p[LocationIndex.MECH_LOC_LA].x, p[LocationIndex.MECH_LOC_LA].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetArmPoints() + "]", p[LocationIndex.MECH_LOC_RA].x, p[LocationIndex.MECH_LOC_RA].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetLegPoints() + "]", p[LocationIndex.MECH_LOC_LL].x, p[LocationIndex.MECH_LOC_LL].y );
        graphics.drawString( "[" + CurMech.GetIntStruc().GetLegPoints() + "]", p[LocationIndex.MECH_LOC_RL].x, p[LocationIndex.MECH_LOC_RL].y );

        // armor information
        p = points.GetArmorInfoPoints();
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_HD ) + "]", p[LocationIndex.MECH_LOC_HD].x, p[LocationIndex.MECH_LOC_HD].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CT ) + "]", p[LocationIndex.MECH_LOC_CT].x, p[LocationIndex.MECH_LOC_CT].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LT ) + "]", p[LocationIndex.MECH_LOC_LT].x, p[LocationIndex.MECH_LOC_LT].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RT ) + "]", p[LocationIndex.MECH_LOC_RT].x, p[LocationIndex.MECH_LOC_RT].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LA ) + "]", p[LocationIndex.MECH_LOC_LA].x, p[LocationIndex.MECH_LOC_LA].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RA ) + "]", p[LocationIndex.MECH_LOC_RA].x, p[LocationIndex.MECH_LOC_RA].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LL ) + "]", p[LocationIndex.MECH_LOC_LL].x, p[LocationIndex.MECH_LOC_LL].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RL ) + "]", p[LocationIndex.MECH_LOC_RL].x, p[LocationIndex.MECH_LOC_RL].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_CTR ) + "]", p[LocationIndex.MECH_LOC_CTR].x, p[LocationIndex.MECH_LOC_CTR].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_LTR ) + "]", p[LocationIndex.MECH_LOC_LTR].x, p[LocationIndex.MECH_LOC_LTR].y );
        graphics.drawString( "[" + CurMech.GetArmor().GetLocationArmor( LocationIndex.MECH_LOC_RTR ) + "]", p[LocationIndex.MECH_LOC_RTR].x, p[LocationIndex.MECH_LOC_RTR].y );
        if( CurMech.GetArmor().GetBAR() < 10 ) {
            graphics.setFont( PrintConsts.XtraSmallFont );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_HD].x, p[LocationIndex.MECH_LOC_HD].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_CT].x - 5, p[LocationIndex.MECH_LOC_CT].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LT].x - 4, p[LocationIndex.MECH_LOC_LT].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RT].x - 4, p[LocationIndex.MECH_LOC_RT].y + 7 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LA].x - 4, p[LocationIndex.MECH_LOC_LA].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RA].x - 5, p[LocationIndex.MECH_LOC_RA].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LL].x - 4, p[LocationIndex.MECH_LOC_LL].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RL].x - 4, p[LocationIndex.MECH_LOC_RL].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_CTR].x + 2, p[LocationIndex.MECH_LOC_CTR].y + 8 );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_LTR].x + 13, p[LocationIndex.MECH_LOC_LTR].y );
            graphics.drawString( "BAR:" + CurMech.GetArmor().GetBAR(), p[LocationIndex.MECH_LOC_RTR].x - 22, p[LocationIndex.MECH_LOC_RTR].y );
            graphics.setFont( PrintConsts.SmallFont );
        }
    }

    private void DrawNonCritable( Graphics2D graphics, String Item, int X, int Y ) {
        // save the old font
        Font OldFont = graphics.getFont();

        // set the new font
        graphics.setFont( PrintConsts.SmallFont );
        graphics.setColor( Grey );
        graphics.drawString( Item, X, Y );
        graphics.setFont( OldFont );
        graphics.setColor( Black );
    }

    private void DrawImages( Graphics2D graphics ) {
        //PrintMech Image
        if( getMechImage() != null ) {
            Dimension d = media.reSize(getMechImage(), 145, 200);
            Point offset = media.offsetImageBottom( new Dimension(145, 200), d);
            graphics.drawImage( getMechImage(), points.GetMechImageLoc().x + offset.x, points.GetMechImageLoc().y + offset.y, d.width, d.height, null );
        }

        if ( LogoImage != null ) {
            graphics.drawImage( LogoImage, points.GetLogoImageLoc().x, points.GetLogoImageLoc().y, 50, 50, null );
        }
    }

    private void DrawGrid( Graphics2D graphics ) {
        graphics.setFont( PrintConsts.ReallySmallFont );
        boolean bPrint = true;
        for (int x = 0; x <= 576; x += 10) {
            if (bPrint) { graphics.drawString(x+"", x-5, 5); }
            bPrint = !bPrint;
            graphics.drawLine(x, 0, x, 756);
        }
        bPrint = false;
        for (int y = 0; y <= 756; y += 10) {
            if (bPrint) { graphics.drawString(y+"", 0, y+5); }
            bPrint = !bPrint;
            graphics.drawLine(0, y, 576, y);
        }
    }

    private Vector GetAmmo() {
        //Output the list of Ammunition
        Vector all = CurMech.GetLoadout().GetNonCore();
        Vector AmmoList = new Vector();
        for ( int index=0; index < all.size(); index++ ) {
            if(  all.get( index ) instanceof Ammunition ) {
                AmmoData CurAmmo = new AmmoData((Ammunition) all.get(index));

                boolean found = false;
                for ( int internal=0; internal < AmmoList.size(); internal++ ) {
                    AmmoData existAmmo = (AmmoData) AmmoList.get(internal);
                    if ( CurAmmo.ActualName.equals( existAmmo.ActualName ) ) {
                        existAmmo.LotSize += CurAmmo.LotSize;
                        found = true;
                        break;
                    }
                }
                if ( !found ) {
                    AmmoList.add(CurAmmo);
                }
            }
        }
        return AmmoList;
    }

    private PlaceableInfo[] SortEquipmentByLocation() {
        Vector v = CurMech.GetLoadout().GetNonCore();
        Vector temp = new Vector();
        abPlaceable[] a = new abPlaceable[v.size()];
        for( int i = 0; i < v.size(); i++ ) {
            if( ! ( v.get( i ) instanceof Ammunition ) ) {
                a[i] = (abPlaceable) v.get( i );
            }
        }

        // now group them by location
        int count = 0;
        abPlaceable b = null;
        PlaceableInfo p = null;
        for( int i = 0; i < a.length; i++ ) {
            if( a[i] != null ) {
                p = new PlaceableInfo();
                b = a[i];
                p.Item = b;
                p.Location = CurMech.GetLoadout().Find( b );
                a[i] = null;
                count ++;
                // search for other matching weapons in the same location
                for( int j = 0; j < a.length; j++ ) {
                    if( a[j] != null ) {
                        if( a[j].CritName().equals( b.CritName() ) ) {
                            if( CurMech.GetLoadout().Find( a[j] ) == p.Location ) {
                                count++;
                                a[j] = null;
                            }
                        }
                    }
                }

                // set the weapon count and add it to the temp vector
                p.Count = count;
                temp.add( p );
                count = 0;
            }
        }

        // produce an array from the vector
        PlaceableInfo[] retval = new PlaceableInfo[temp.size()];
        for( int i = 0; i < temp.size(); i++ ) {
            retval[i] = (PlaceableInfo) temp.get( i );
        }
        return retval;
    }

    private void GetRecordSheet( ImageTracker images ) {
        // loads the correct record sheet and points based on the information given
        RecordSheet = images.getImage( PrintConsts.RS_TW_BP );
        ChartImage = images.getImage(PrintConsts.BP_ChartImage );
        points = new TWBipedPoints();

        if ( CurMech.IsQuad() ) {
            RecordSheet = images.getImage( PrintConsts.RS_TW_QD );
            if ( CurMech.IsQuad() ) { ChartImage = images.getImage(PrintConsts.QD_ChartImage); }
            points = new TWQuadPoints();
        }

        if ( Advanced ) {
            RecordSheet = images.getImage( PrintConsts.RS_TO_BP );
        }
    }

    private String GetPrintName( abPlaceable a ) {
        // returns a modified PrintName, useful for special situations such as
        // mixed-tech mechs.
        String retval = a.CritName();
        if( a instanceof RangedWeapon && CurMech.GetLoadout().GetTechBase() == AvailableCode.TECH_BOTH ) {
            switch( ((RangedWeapon) a).GetTechBase() ) {
                case AvailableCode.TECH_INNER_SPHERE:
                    retval = "(IS) " + retval;
                    break;
                case AvailableCode.TECH_CLAN:
                    retval = "(CL) " + retval;
                    break;
            }
        }
        return retval;
    }

    private class PlaceableInfo {
        public int Location,
                   Count;
        public abPlaceable Item;
    }

    private class AmmoData {
        public String ActualName,
                      ChatName,
                      CritName,
                      LookupName;
        public int LotSize;

        public AmmoData( Ammunition ammo ) {
            this.ActualName = ammo.ActualName();
            this.ChatName = ammo.ChatName();
            this.CritName = ammo.CritName().replace("@", "").trim();
            this.LookupName = ammo.LookupName();

            this.LotSize = ammo.GetLotSize();
        }

        public String Format() {
            return Prefs.get( "AmmoNamePrintFormat", "@%P").replace("%P", CritName).replace("%F", LookupName).replace("%L", "");
        }
    }
}
