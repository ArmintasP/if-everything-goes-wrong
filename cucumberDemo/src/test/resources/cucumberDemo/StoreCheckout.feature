Feature: Checkout feature
  A simple checkout feature.

#  Scenario: Add a product to the cart
#    Given the user logins using valid credentials
#    When he browses "Phones" category
#    And selects an "Iphone"
#    And clicks `Add to cart`
#    Then he sees "Product added."

  Scenario Outline: Add a product to the cart
    Given the user logins using valid credentials
    When he browses "<category>" category
    And selects an "<product>"
    And clicks `Add to cart`
    Then he sees "Product added."

    Examples:
      | category | product |
      | Phones   | Iphone  |
      | Laptops  | Sony    |