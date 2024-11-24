package com.tribbloids.spike.dotty.issue

trait TypeBound_Minimal {

  type Min
  type Max >: Min

//  type Under = TypeBound_Minimal.K[? >: this.Min, ? <: this.Max] // <-- error happened here

  type Under_Dual1 = TypeBound_Minimal.Lt[Min, Max] // <- no error despite equivalent

  type Under_Dual2 = TypeBound_Minimal { // <- no error despite equivalent
    type Min >: TypeBound_Minimal.this.Min
    type Max <: TypeBound_Minimal.this.Max
  }

  trait TypeBound_Dual {
    type _Min = TypeBound_Minimal.this.Min
    type _Max = TypeBound_Minimal.this.Max

    type Under = TypeBound_Dual { // <- no error despite equivalent
      type _Min >: TypeBound_Dual.this._Min
      type _Max <: TypeBound_Dual.this._Max
    }
  }
}

object TypeBound_Minimal {

  type K[TMin, TMax >: TMin] = TypeBound_Minimal {
    type Min = TMin
    type Max = TMax
  }

  type Lt[TMin, TMax >: TMin] = TypeBound_Minimal {
    type Min >: TMin
    type Max <: TMax
  }
}
