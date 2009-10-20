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

package components;

/**
 *
 * @author Michael Mills
 */
public class IndustrialEquipment extends Equipment{
    private EquipmentValidationInterface validator;
    private String validationFalseMessage;
    public IndustrialEquipment ( String actualname, String lookupname, String critname, String t, AvailableCode a, EquipmentValidationInterface validator, String vf){
        super( actualname, lookupname, critname, t, a );
        this.validator = validator;
        validationFalseMessage = vf;
    }

    private IndustrialEquipment( IndustrialEquipment i ) {
        super( (Equipment) i );
        validator = i.validator;
        validationFalseMessage = i.validationFalseMessage;
    }

    public EquipmentValidationInterface getValidator(){
            return validator;
    }
    
    public boolean validate (Mech m){
            return validator.validate(m);
    }

    public String getValidationFalseMessage(){
            return validationFalseMessage;
    }

    @Override
    public IndustrialEquipment Clone() {
        return new IndustrialEquipment( this );
    }
}
