package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PuwenBloodhelm extends Ally {

  public PuwenBloodhelm(Card cloneFrom) {
    super(cloneFrom);
  }
}