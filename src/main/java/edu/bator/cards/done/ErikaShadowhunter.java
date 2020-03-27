package edu.bator.cards.done;

import edu.bator.cards.Ally;
import edu.bator.cards.Card;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ErikaShadowhunter extends Ally {

  public ErikaShadowhunter(Card cloneFrom) {
    super(cloneFrom);
  }
}