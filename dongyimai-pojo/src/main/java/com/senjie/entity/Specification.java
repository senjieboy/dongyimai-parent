package com.senjie.entity;

import com.senjie.pojo.TbSpecification;
import com.senjie.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SenJie
 * @Data 2021/4/1 22:09
 */
public class Specification  implements Serializable {


    private TbSpecification specification;
    private List<TbSpecificationOption> specificationOptionList;

    public Specification () {
        super();
    }

    public Specification (TbSpecification specification, List<TbSpecificationOption> specificationOptionList) {
        super();
        this.specification = specification;
        this.specificationOptionList = specificationOptionList;
    }

    public TbSpecification getSpecification () {
        return specification;
    }

    public void setSpecification (TbSpecification specification) {
        this.specification = specification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList () {
        return specificationOptionList;
    }

    public void setSpecificationOptionList (List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
