module ReportsHelper

  def get_report_partial_name(report)
     report.type_str.underscore + '_fields'
  end


  def get_report_partial_div_name(report)
     report.to_s.underscore + '_options'
  end


  #generuje kod javascript ukrywajacy i pokazujacy divy
  # zawierajace formularze dla poszczegolnych typow raportow
  #@author Jarek
  def control_visibility_of_report_forms(report)
    [FlowReport, ValueReport, ShareReport].inject '' do |mem, item|
      div = get_report_partial_div_name(item)
      command = report.is_a?(item)? "show" : "hide"
      mem += "Element.#{command}('#{div}');"
    end
  end

  def get_desc_for_report_view_type(code)
    case code
    when :bar then 'Wykres słupkowy'
    when :pie then 'Wykres kołowy'
    when :linear then 'Wykres liniowy'
    when :text then 'Raport tekstowy'
    end
  end

  def get_desc_for_share_type(code)
    case code
    when :percentage then 'Procentowy'
    when :value then '"Wartościowy"'
    end
  end

  def get_desc_for_inclusion_type(code)
    case code
    when :none then 'Nie wybrana'
    when :both then 'Kategoria i podkategorie osobno'
    when :category_only then 'Kategoria'
    when :category_and_subcategories then 'Kategoria (+podkategorie)'
    end
  end

  def get_desc_for_period_type(code)
    case code
    when :day then 'Dzień'
    when :week then 'Tydzień'
    when :month then 'Miesiąc'
    when :quarter then 'Kwartał'
    when :year then 'Rok'
    when :custom then 'Dowolny'
    end
  end

  def get_desc_for_period_division(code)
    case code
    when :day then 'Dzień'
    when :week then 'Tydzień'
    when :month then 'Miesiąc'
    when :quarter then 'Kwartał'
    when :year then 'Rok'
    when :none then 'Bez podziału'
    end
  end

  def sort_category_report_options(cat_rep_opts)
    cat_rep_opts.sort do |a,b|
      a_cat = a.category
      b_cat = b.category
      (a_cat.category_type_int <=> b_cat.category_type_int).nonzero? || (a_cat.lft <=> b_cat.lft)
    end
  end

end
